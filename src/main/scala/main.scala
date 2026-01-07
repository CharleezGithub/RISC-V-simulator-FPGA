import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV extends Module {
  val io = IO(new Bundle {

  })
  // Instantiate all stages so we can wire them together 
  val fetch = Module(new Fetch)
  val pipeline1 = Module(new IF_ID)
  val decoder = Module(new Decode)
  val pipeline2 = Module(new ID_EX)
  val execute = Module(new Execute)
  val pipeline3 = Module(new EX_MEM)


  // Connecting Fetch - pipeline registers
  pipeline1.io.instrIn := fetch.io.instr
  pipeline1.io.pcIn := fetch.io.pcOut

  // Connecting pipeline registers - Decoder
  decoder.io.instrIn := pipeline1.io.instr
  decoder.io.pcIn := pipeline1.io.pcOut 

  // Connecting Decoder - pipeline registers
  pipeline2.io.rs1In := decoder.io.rs1Out
  pipeline2.io.rs2In := decoder.io.rs2Out
  pipeline2.io.isADDIn := decoder.io.isADDOut
  pipeline2.io.pcIn := decoder.io.pcOut

  // Connecting pipeline registers - Execute
  execute.io.rs1In := pipeline2.io.rs1Out
  execute.io.rs2In := pipeline2.io.rs2Out
  execute.io.isADDIn := pipeline2.io.isADDOut
  execute.io.pcIn := pipeline2.io.pcOut
  
  // Connecting Execute - pipeline registers 
  pipeline3.io.pcIn := execute.io.pcOut
  pipeline3.io.ALUout := execute.io.ALUout
}

// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
