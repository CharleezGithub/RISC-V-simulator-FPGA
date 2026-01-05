import chisel3._
import chisel3.util._
import chisel.lib.uart._

class UArt(frequ: Int) extends Module {
  val io = IO(new Bundle {
    val tx = Output(Bool())
    val canStock = Input(UInt(4.W))
    val add = Input(UInt(2.W))
    val buy = Input(Bool())
  })

  val uart = Module(new BufferedTx(frequ, 115200))
  io.tx := uart.io.txd

  // Function to rewrite our string messages to vectors
  val messageLength = 40
  def strToVecPadded(msg: String, length: Int): Vec[UInt] = {
    val padded = msg.padTo(length, ' ').map(_.U(8.W))
    VecInit(padded)
  }

  // Pre-set messages to be selected on the basis of the FSM signals
  val messages = VecInit(
    Seq(
      strToVecPadded("2 Added to Sum", messageLength), // 0
      strToVecPadded("5 Added to Sum", messageLength), // 1
      strToVecPadded("Stock is low restock recommended", messageLength), // 2
      strToVecPadded("Stock is depleted restock required", messageLength), // 3
      strToVecPadded("Someone bought a can!", messageLength) // 4
    )
  )

  val msgIndex = RegInit(0.U(3.W))
  val index = RegInit(0.U(6.W))
  val sending = RegInit(false.B)
  val currentMsg = messages(msgIndex)
  val len = currentMsg.length.U

  // To avoid sending multiple low stock messages in a row
  val lowStockSent = RegInit(false.B)
  val emptyStockSent = RegInit(false.B)

//-------------------------------------------------------------------------------------------
//                                     Printing-loop
//-------------------------------------------------------------------------------------------
  uart.io.channel.bits := currentMsg(index)
  uart.io.channel.valid := sending && index =/= len
  when(uart.io.channel.ready && sending && index =/= len) {
    index := index + 1.U
  }
  when(index === len) {
    sending := false.B // Stop sending after last character
  }

  when(io.canStock =/= 3.U) {
    lowStockSent := false.B
  }
  when(io.canStock =/= 0.U) {
    emptyStockSent := false.B
  }
//-------------------------------------------------------------------------------------------
//                                     Besked-Selector
//-------------------------------------------------------------------------------------------
  when(!sending) {
    when(io.add === "b01".U) {
      msgIndex := 0.U; index := 0.U;
      sending := true.B // Also sets index to zero to start over each time we print
    }.elsewhen(io.add === "b10".U) {
      msgIndex := 1.U; index := 0.U; sending := true.B
    }.elsewhen(io.canStock === 3.U && lowStockSent === false.B) {
      msgIndex := 2.U; index := 0.U; sending := true.B; lowStockSent := true.B
    }.elsewhen(io.canStock === 0.U && emptyStockSent === false.B) {
      msgIndex := 3.U; index := 0.U; sending := true.B; emptyStockSent := true.B
    }.elsewhen(io.buy) {
      msgIndex := 4.U; index := 0.U; sending := true.B
    }
  }
}

// Verilog generation
object UArtMain extends App {
  emitVerilog(new UArt(100_000_000), Array("--target-dir", "generated"))
}
