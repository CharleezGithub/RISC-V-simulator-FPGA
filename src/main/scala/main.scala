import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV() extends Module {
  val io = IO(new Bundle {
    val pc = Output(UInt(32.W))
    val addr = Output(UInt(8.W))
  })

  // Value definitions
  val pc = RegInit(0.U(32.W))
  val addr = RegInit(0.U(8.W))
  
}
// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
