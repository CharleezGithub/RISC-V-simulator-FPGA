package empty

import chisel3._
import chisel3.util._
import chisel.lib.uart._

/** For every received byte, transmit a selected response */
class Uart(frequ: Int, baud: Int = 115200) extends Module {
    val io = IO(new Bundle {
        val rx = Input(Bool())
        val sendResponse = Input(Bool())
        val responseType = Input(UInt(4.W))

        val busy = Output(Bool())

        val profilingData = Input(Vec(3, UInt(32.W)))
        val dataMemory = Input(Vec(64, UInt(32.W)))
        val printOutRegs = Input(Vec(32, UInt(32.W)))

        val captureEnable = Input(Bool())
        val clearBuffer = Input(Bool())

        val rxReadValue = Output(UInt(8.W))
        val rxValid = Output(Bool())
        val tx = Output(Bool())
    })

    // ------------------------------------------------------------
    // UART
    // ------------------------------------------------------------
    val rxm = Module(new Rx(frequ, baud))
    val txm = Module(new BufferedTx(frequ, baud))

    rxm.io.rxd := io.rx.asUInt
    io.tx := txm.io.txd.asBool
    io.rxReadValue := rxm.io.channel.bits
    io.rxValid := RegNext(rxm.io.channel.valid, false.B)

    // ------------------------------------------------------------
    // RX capture buffer
    // ------------------------------------------------------------
    val rxStored = RegInit(VecInit(Seq.fill(32)(0.U(8.W))))
    val rxCount = RegInit(0.U(6.W))

    val rxFire = rxm.io.channel.valid && rxm.io.channel.ready

    when(io.clearBuffer) {
        rxCount := 0.U
    }

    when(io.captureEnable && rxFire && rxCount < 32.U) {
        rxStored(rxCount) := rxm.io.channel.bits
        rxCount := rxCount + 1.U
    }

    // RX queue so we never stall RX
    val inQ = Module(new Queue(UInt(8.W), entries = 32))
    inQ.io.enq.valid := rxm.io.channel.valid && io.captureEnable
    inQ.io.enq.bits := rxm.io.channel.bits
    rxm.io.channel.ready := Mux(io.captureEnable, inQ.io.enq.ready, true.B)
    inQ.io.deq.ready := false.B

    // ------------------------------------------------------------
    // Constants & helpers
    // ------------------------------------------------------------
    val MAX_LEN = 64
    val REG_LINE_LEN = 11
    val MEM_LINE_LEN = 23
    val MEM_ENTRIES = 64

    def strToVecPadded(s: String): Vec[UInt] =
        VecInit(s.padTo(MAX_LEN, ' ').map(_.toInt.U(8.W)))

    val rxStoredPadded = Wire(Vec(MAX_LEN, UInt(8.W)))
    rxStoredPadded := VecInit(rxStored ++ Seq.fill(MAX_LEN - 32)(0.U))

    // Fast hex LUT (no compares/adds)
    val hexLut = VecInit("0123456789ABCDEF".map(_.toInt.U(8.W)))
    def nibbleToAscii(n: UInt): UInt = hexLut(n)

    // ------------------------------------------------------------
    // Profiling strings (3 × 11 bytes)
    // ------------------------------------------------------------
    def u32Line(x: UInt): Vec[UInt] =
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

    val printOutProfilingPadded = Wire(Vec(MAX_LEN, UInt(8.W)))
    printOutProfilingPadded :=
        VecInit(
            u32Line(io.profilingData(0)) ++
                u32Line(io.profilingData(1)) ++
                u32Line(io.profilingData(2)) ++
                Seq.fill(MAX_LEN - 33)(0.U)
        )

    // ------------------------------------------------------------
    // Register & memory print helpers (NO divide/modulo)
    // ------------------------------------------------------------
    def regPrintByte(regIdx: UInt, off: UInt): UInt = {
        val v = io.printOutRegs(regIdx)
        val h = Wire(Vec(8, UInt(8.W)))
        for (i <- 0 until 8) {
            h(i) := nibbleToAscii(v(31 - 4 * i, 28 - 4 * i))
        }

        MuxLookup(
            off,
            0.U,
            Array(
                0.U -> '0'.U,
                1.U -> 'x'.U,
                2.U -> h(0),
                3.U -> h(1),
                4.U -> h(2),
                5.U -> h(3),
                6.U -> h(4),
                7.U -> h(5),
                8.U -> h(6),
                9.U -> h(7),
                10.U -> '\n'.U
            )
        )
    }

    def memPrintByte(entryIdx: UInt, off: UInt): UInt = {
        val addr = (entryIdx << 2).asUInt.pad(32)
        val data = io.dataMemory(entryIdx)

        val ah = Wire(Vec(8, UInt(8.W)))
        val dh = Wire(Vec(8, UInt(8.W)))
        for (i <- 0 until 8) {
            ah(i) := nibbleToAscii(addr(31 - 4 * i, 28 - 4 * i))
            dh(i) := nibbleToAscii(data(31 - 4 * i, 28 - 4 * i))
        }

        val b = MuxLookup(
            off,
            0.U,
            Array(
                0.U -> '0'.U,
                1.U -> 'x'.U,
                2.U -> ah(0),
                3.U -> ah(1),
                4.U -> ah(2),
                5.U -> ah(3),
                6.U -> ah(4),
                7.U -> ah(5),
                8.U -> ah(6),
                9.U -> ah(7),
                10.U -> ':'.U,
                11.U -> ' '.U,
                12.U -> '0'.U,
                13.U -> 'x'.U,
                14.U -> dh(0),
                15.U -> dh(1),
                16.U -> dh(2),
                17.U -> dh(3),
                18.U -> dh(4),
                19.U -> dh(5),
                20.U -> dh(6),
                21.U -> dh(7),
                22.U -> '\n'.U
            )
        )

        Mux(data === 0.U, 0.U, b)
    }

    // ------------------------------------------------------------
    // Response metadata
    // ------------------------------------------------------------
    val responseLengths = VecInit(
        Seq(
            20.U, // Program Received
            33.U, // Profiling
            rxCount, // RX echo
            (32 * 11).U, // Registers
            (64 * 23).U // Memory
        )
    )

    val resp0 = strToVecPadded("Program Received!\r\n")
    val resp1 = printOutProfilingPadded
    val resp2 = rxStoredPadded

    // ------------------------------------------------------------
    // TX control & pipelined byte generator
    // ------------------------------------------------------------
    val sending = RegInit(false.B)
    io.busy := sending

    val sendPrev = RegNext(io.sendResponse, false.B)
    val sendRise = io.sendResponse && !sendPrev

    val latchedResponseType = RegInit(0.U(4.W))
    when(sendRise && !sending) {
        latchedResponseType := io.responseType
    }

    val msgSel = Mux(latchedResponseType <= 3.U, latchedResponseType, 4.U)
    val msgLen = responseLengths(msgSel)

    // Counters (small, fast)
    val idxStr = RegInit(0.U(6.W))
    val regIdx = RegInit(0.U(5.W))
    val regOff = RegInit(0.U(4.W))
    val memIdx = RegInit(0.U(6.W))
    val memOff = RegInit(0.U(5.W))

    // Prefetch buffer (breaks critical path)
    val outByte = RegInit(0.U(8.W))
    val outValid = RegInit(false.B)
    val lastInFlight = RegInit(false.B)

    txm.io.channel.valid := sending && outValid
    txm.io.channel.bits := outByte

    val txFire = txm.io.channel.valid && txm.io.channel.ready

    when(sendRise && !sending) {
        sending := true.B
        outValid := false.B
        lastInFlight := false.B
        idxStr := 0.U
        regIdx := 0.U; regOff := 0.U
        memIdx := 0.U; memOff := 0.U
    }

    val nextByte = Wire(UInt(8.W))
    nextByte := 0.U

    when(msgSel === 0.U) { nextByte := resp0(idxStr) }
        .elsewhen(msgSel === 1.U) { nextByte := resp1(idxStr) }
        .elsewhen(msgSel === 2.U) { nextByte := resp2(idxStr) }
        .elsewhen(msgSel === 3.U) { nextByte := regPrintByte(regIdx, regOff) }
        .otherwise { nextByte := memPrintByte(memIdx, memOff) }

    val strLast = idxStr === (msgLen - 1.U)(5, 0)
    val regLast = regIdx === 31.U && regOff === (REG_LINE_LEN - 1).U
    val memLast =
        memIdx === (MEM_ENTRIES - 1).U && memOff === (MEM_LINE_LEN - 1).U

    val loadNext = sending && (!outValid || txFire)

    when(loadNext) {
        outByte := nextByte
        outValid := true.B

        lastInFlight :=
            (msgSel <= 2.U && strLast) ||
                (msgSel === 3.U && regLast) ||
                (msgSel === 4.U && memLast)

        when(msgSel <= 2.U) {
            when(!strLast) { idxStr := idxStr + 1.U }
        }.elsewhen(msgSel === 3.U) {
            when(regOff === (REG_LINE_LEN - 1).U) {
                regOff := 0.U; regIdx := regIdx + 1.U
            }.otherwise {
                regOff := regOff + 1.U
            }
        }.otherwise {
            when(memOff === (MEM_LINE_LEN - 1).U) {
                memOff := 0.U; memIdx := memIdx + 1.U
            }.otherwise {
                memOff := memOff + 1.U
            }
        }
    }

    when(txFire && lastInFlight) {
        sending := false.B
        outValid := false.B
        lastInFlight := false.B
    }
}
