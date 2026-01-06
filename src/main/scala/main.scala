import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV extends Module {
  val io = IO(new Bundle {})

  // =========================================================
  // Module instances
  // =========================================================
  val ifStage  = Module(new IFStage)
  val if_id    = Module(new IF_ID)

  val decoder  = Module(new Decoder)
  val regFile  = Module(new RegFile)
  val id_ex    = Module(new ID_EX)

  val alu      = Module(new ALU)
  val ex_mem   = Module(new EX_MEM)

  val mem_wb   = Module(new MEM_WB)

  // =========================================================
  // IF STAGE
  // =========================================================
  ifStage.io.pcNext := ifStage.io.pc + 4.U

  // =========================================================
  // IF / ID PIPELINE REGISTER
  // =========================================================
  if_id.io.pcIn     := ifStage.io.pc
  if_id.io.instrIn := ifStage.io.instr

  // =========================================================
  // ID STAGE
  // =========================================================
  decoder.io.instr := if_id.io.instrOut

  regFile.io.rs1 := decoder.io.rs1
  regFile.io.rs2 := decoder.io.rs2

  // =========================================================
  // ID / EX PIPELINE REGISTER
  // =========================================================
  id_ex.io.aluOpIn   := decoder.io.aluOp
  id_ex.io.rs1ValIn := regFile.io.rs1Data
  id_ex.io.rs2ValIn := regFile.io.rs2Data
  id_ex.io.rdIn     := decoder.io.rd
  id_ex.io.immIn    := decoder.io.imm

  // =========================================================
  // EX STAGE
  // =========================================================
  alu.io.aluOp := id_ex.io.aluOpOut
  alu.io.a     := id_ex.io.rs1ValOut
  alu.io.b     := id_ex.io.rs2ValOut

  // =========================================================
  // EX / MEM PIPELINE REGISTER
  // =========================================================
  ex_mem.io.aluOutIn := alu.io.out
  ex_mem.io.rdIn    := id_ex.io.rdOut

  // =========================================================
  // MEM STAGE
  // =========================================================
  // (No real memory yet — pass-through)

  // =========================================================
  // MEM / WB PIPELINE REGISTER
  // =========================================================
  mem_wb.io.dataIn := ex_mem.io.aluOutOut
  mem_wb.io.rdIn   := ex_mem.io.rdOut

  // =========================================================
  // WB STAGE
  // =========================================================
  regFile.io.rd     := mem_wb.io.rdOut
  regFile.io.rdData := mem_wb.io.dataOut
  regFile.io.we     := true.B
}

// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
