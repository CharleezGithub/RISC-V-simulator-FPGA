import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV() extends Module {
  val io = IO(new Bundle {
    val tx = Output(Bool())
  })

  val uart = Module(new UArt(100000000))

  // UART
  uart.io.add := false.B
  uart.io.buy := false.B
  uart.io.canStock := 12.U
  io.tx := uart.io.tx

}
// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
