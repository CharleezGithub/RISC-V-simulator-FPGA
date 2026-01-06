import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV extends Module {
  val io = IO(new Bundle {
    
  })

}

// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
