import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV extends Module {
  val io = IO(new Bundle {

  })
  // Instantiate all stages so we can wire them together 
  val fetch = Module(new Fetch)
  val pipelineRegs = Module (new IF_ID)
  val decoder = Module(new Decode)


  // Connecting Fetch - pipeline registers
  pipelineRegs.io.instrIn := fetch.io.instr
  pipelineRegs.io.pcIn := fetch.io.pcOut

  // Connecting pipeline registers - Decoder
  decoder.io.instrIn := pipelineRegs.io.instr
  decoder.io.pcIn := pipelineRegs.io.pcOut 
}

// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
