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
        ) // 0 for "Program Received", 1 for profiling (Not Implemented), 2 for debug print out everything received, 3 for print out registers

        val printOutRegs = Input(Vec(1, UInt(32.W))) // Only 1 register for now
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

    val regValue = io.printOutRegs(0)
    val hexBytes = Wire(Vec(11, UInt(8.W))) // "0xXXXXXXXX\n"
    hexBytes(0) := '0'.U
    hexBytes(1) := 'x'.U
    hexBytes(2) := nibbleToAscii(regValue(31, 28))
    hexBytes(3) := nibbleToAscii(regValue(27, 24))
    hexBytes(4) := nibbleToAscii(regValue(23, 20))
    hexBytes(5) := nibbleToAscii(regValue(19, 16))
    hexBytes(6) := nibbleToAscii(regValue(15, 12))
    hexBytes(7) := nibbleToAscii(regValue(11, 8))
    hexBytes(8) := nibbleToAscii(regValue(7, 4))
    hexBytes(9) := nibbleToAscii(regValue(3, 0))
    hexBytes(10) := '\n'.U

    val printOutRegsPadded = Wire(Vec(MAX_LEN, UInt(8.W)))
    printOutRegsPadded := VecInit(hexBytes ++ Seq.fill(MAX_LEN - 11)(0.U(8.W)))

    val responses = VecInit(
        Seq(
            strToVecPadded("Program Received!\r\n"),
            strToVecPadded("Dummy Profiling Data! (Not Implemented Yet!)\r\n"),
            rxStoredPadded, // already Vec(32, UInt(8.W)) — pad rxStored to MAX_LEN too if needed
            printOutRegsPadded,
            strToVecPadded("Invalid Response Type!\r\n")
        )
    )

    val responseLengths = VecInit(
        Seq(
            20.U, // "Program Received!\r\n"
            45.U, // Profiling message
            rxCount, // RX buffer length
            11.U, // printRegs (32-bit register = 4 bytes)
            26.U // Invalid Response Type
        )
    )

    val idx = RegInit(0.U(6.W))
    val sending = RegInit(false.B)
    val latchedResponseType = RegInit(0.U(4.W))

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
        txm.io.channel.bits := msgVec(idx)

        when(txm.io.channel.ready) {
            when(idx === msgLen - 1.U) {
                sending := false.B
            }.otherwise {
                idx := idx + 1.U
            }
        }
    }
}
