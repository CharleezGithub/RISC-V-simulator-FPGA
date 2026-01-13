import chisel3._
import chisel3.util._
import chisel.lib.uart._

/** For every received byte, transmit "Key Received!\r\n" */
class Uart(frequ: Int, baud: Int = 115200) extends Module {
    val io = IO(new Bundle {
        val rx = Input(Bool())
        val sendResponse = Input(Bool())
        val responseType = Input(
            UInt(4.W)
        ) // 0 for "Program Received", 1 for profiling, 2 for debug print out everything received, 3 for print out registers, 4 invalid

        val busy = Output(Bool())

        // Profiling
        val profilingData = Input(
            Vec(3, UInt(32.W))
        ) // Clock Count, MemReadCount, MemWriteCount

        val dataMemory = Input(Vec(64, UInt(32.W)))

        val printOutRegs = Input(Vec(32, UInt(32.W))) // Only 1 register for now
        val captureEnable = Input(Bool())
        val clearBuffer = Input(Bool())
        val rxReadValue = Output(UInt(8.W))
        val rxValid = Output(Bool())
        val tx = Output(Bool())
    })

    val MAX_LEN = 64

    val rxm = Module(new Rx(frequ, baud))
    val txm = Module(new BufferedTx(frequ, baud))

    val rxStored = RegInit(VecInit(Seq.fill(32)(0.U(8.W))))
    val rxCount = RegInit(0.U(6.W)) // up to 32 bytes

    io.rxValid := RegNext(rxm.io.channel.valid, false.B)

    rxm.io.rxd := io.rx.asUInt
    io.tx := txm.io.txd.asBool

    io.rxReadValue := rxm.io.channel.bits

    // Add read values to this Register array to be printed out later
    val rxFire = rxm.io.channel.valid && rxm.io.channel.ready

    // Reset buffer when ready-to-read phase begins
    when(io.clearBuffer) {
        rxCount := 0.U
    }

    when(io.captureEnable && rxFire && rxCount < 32.U) {
        rxStored(rxCount) := rxm.io.channel.bits
        rxCount := rxCount + 1.U
    }

    // Queue incoming bytes so we don't drop them while doing other stuff
    val inQ = Module(new Queue(UInt(8.W), entries = 32))

    inQ.io.enq.valid := rxm.io.channel.valid && io.captureEnable
    inQ.io.enq.bits := rxm.io.channel.bits
    rxm.io.channel.ready := Mux(io.captureEnable, inQ.io.enq.ready, true.B)

    def strToVecPadded(s: String): Vec[UInt] =
        VecInit(s.padTo(MAX_LEN, ' ').map(_.toInt.U(8.W)))

    val rxStoredPadded = Wire(Vec(MAX_LEN, UInt(8.W)))
    rxStoredPadded := VecInit(rxStored ++ Seq.fill(MAX_LEN - 32)(0.U))

    // Convert a 4-bit nibble to ASCII hex character
    def nibbleToAscii(nibble: UInt): UInt = {
        Mux(
            nibble < 10.U,
            nibble + 0x30.U,
            nibble - 10.U + 0x41.U
        ) // '0'-'9' or 'A'-'F'
    }

    def u32ToHexLine(x: UInt): Vec[UInt] = {
        VecInit(
            Seq(
                '0'.U,
                'x'.U,
                nibbleToAscii(x(31, 28)),
                nibbleToAscii(x(27, 24)),
                nibbleToAscii(x(23, 20)),
                nibbleToAscii(x(19, 16)),
                nibbleToAscii(x(15, 12)),
                nibbleToAscii(x(11, 8)),
                nibbleToAscii(x(7, 4)),
                nibbleToAscii(x(3, 0)),
                '\n'.U
            )
        )
    }

    // 32 regs × "0xXXXXXXXX\n" (11 bytes each) = 352 bytes
    def regPrintByte(i: UInt): UInt = {
        val regIdx = (i / 11.U)(4, 0) // 0..31
        val off = (i % 11.U)(3, 0) // 0..10
        val v = io.printOutRegs(regIdx)
        MuxLookup(
            off,
            0.U,
            Array(
                0.U -> '0'.U,
                1.U -> 'x'.U,
                2.U -> nibbleToAscii(v(31, 28)),
                3.U -> nibbleToAscii(v(27, 24)),
                4.U -> nibbleToAscii(v(23, 20)),
                5.U -> nibbleToAscii(v(19, 16)),
                6.U -> nibbleToAscii(v(15, 12)),
                7.U -> nibbleToAscii(v(11, 8)),
                8.U -> nibbleToAscii(v(7, 4)),
                9.U -> nibbleToAscii(v(3, 0)),
                10.U -> '\n'.U
            )
        )
    }

    // Print non-zero data memory as "0xADDR: 0xDATA\n"
    def memPrintByte(i: UInt): UInt = {
        val entryIdx = (i / 22.U)(5, 0)
        val off = (i % 22.U)(4, 0)

        val addr = (entryIdx << 2).asUInt.pad(32)
        val data = io.dataMemory(entryIdx)

        Mux(
            data === 0.U,
            0.U,
            MuxLookup(
                off,
                0.U,
                Array(
                    0.U -> '0'.U,
                    1.U -> 'x'.U,
                    2.U -> nibbleToAscii(addr(31, 28)),
                    3.U -> nibbleToAscii(addr(27, 24)),
                    4.U -> nibbleToAscii(addr(23, 20)),
                    5.U -> nibbleToAscii(addr(19, 16)),
                    6.U -> nibbleToAscii(addr(15, 12)),
                    7.U -> nibbleToAscii(addr(11, 8)),
                    8.U -> nibbleToAscii(addr(7, 4)),
                    9.U -> nibbleToAscii(addr(3, 0)),
                    10.U -> ':'.U,
                    11.U -> ' '.U,
                    12.U -> '0'.U,
                    13.U -> 'x'.U,
                    14.U -> nibbleToAscii(data(31, 28)),
                    15.U -> nibbleToAscii(data(27, 24)),
                    16.U -> nibbleToAscii(data(23, 20)),
                    17.U -> nibbleToAscii(data(19, 16)),
                    18.U -> nibbleToAscii(data(15, 12)),
                    19.U -> nibbleToAscii(data(11, 8)),
                    20.U -> nibbleToAscii(data(7, 4)),
                    21.U -> nibbleToAscii(data(3, 0)),
                    22.U -> '\n'.U
                )
            )
        )
    }

    val printOutProfilingPadded = Wire(Vec(MAX_LEN, UInt(8.W)))

    val clkLine = u32ToHexLine(io.profilingData(0))
    val rdLine = u32ToHexLine(io.profilingData(1))
    val wrLine = u32ToHexLine(io.profilingData(2))

    printOutProfilingPadded := VecInit(
        clkLine ++ rdLine ++ wrLine ++
            Seq.fill(MAX_LEN - 33)(0.U)
    )

    val responses = VecInit(
        Seq(
            strToVecPadded("Program Received!\r\n"),
            printOutProfilingPadded,
            rxStoredPadded, // already Vec(32, UInt(8.W)) — pad rxStored to MAX_LEN too if needed
            strToVecPadded(""), // placeholder for printRegs
            strToVecPadded("")
        )
    )

    val responseLengths = VecInit(
        Seq(
            20.U, // "Program Received!\r\n"
            33.U, // Profiling
            rxCount, // RX buffer length
            (32 * 11).U, // printRegs (32 registers)
            (64 * 22).U // Print out the memory
        )
    )

    val idx = RegInit(0.U(10.W))
    val sending = RegInit(false.B)
    val latchedResponseType = RegInit(0.U(4.W))

    io.busy := sending

    // Rising edge detect for sendResponse
    val sendPrev = RegNext(io.sendResponse, false.B)
    val sendRise = io.sendResponse && !sendPrev

    // Latch responseType when we start sending
    when(sendRise && !sending) {
        latchedResponseType := io.responseType
    }

    val msgSel = Mux(latchedResponseType <= 3.U, latchedResponseType, 4.U)
    val msgLen = responseLengths(msgSel)
    val msgVec = responses(msgSel)

    val msgByte = Wire(UInt(8.W))
    msgByte := msgVec(idx)
    when(msgSel === 3.U) {
        msgByte := regPrintByte(idx)
    }.elsewhen(msgSel === 4.U) {
        msgByte := memPrintByte(idx)
    }

    // Default TX signals
    txm.io.channel.valid := false.B
    txm.io.channel.bits := 0.U

    // Start sending when we want to send
    when(sendRise && !sending) {
        sending := true.B
        idx := 0.U
    }

    // RX queue no longer controls TX
    inQ.io.deq.ready := false.B

    // Stream the message out, one char at a time
    when(sending) {
        txm.io.channel.valid := true.B
        txm.io.channel.bits := msgByte

        when(txm.io.channel.ready) {
            txm.io.channel.valid := true.B
            txm.io.channel.bits := msgByte
            when(idx === msgLen - 1.U) {
                sending := false.B
            }.otherwise {
                idx := idx + 1.U
            }
        }
    }
}
