import chisel3._
import chisel3.util._

class UartBootloader extends Module {
  val io = IO(new Bundle {
    // Control
    val start = Input(Bool())
    val done = Output(Bool())

    // UART RX interface
    val rxValid = Input(Bool())
    val rxData = Input(UInt(8.W))

    // Memory write interface
    val memWriteEn = Output(Bool())
    val memWriteAddr = Output(UInt(32.W))
    val memWriteData = Output(UInt(8.W))
  })

  val addr = RegInit(0.U(32.W))
  val active = RegInit(false.B)
  val doneReg = RegInit(false.B)
  io.memWriteEn := false.B
  io.memWriteAddr := addr
  io.memWriteData := io.rxData
  io.done := doneReg

  when(io.start) {
    active := true.B
    addr := 0.U
    doneReg := false.B
  }

  // Escape sequence
  when(io.rxData === 0xff.U && io.rxValid && active) {
    doneReg := true.B
    active := false.B
  }.otherwise {
    when(active && io.rxValid) {
      io.memWriteEn := true.B
      addr := addr + 1.U
    }
  }
}
