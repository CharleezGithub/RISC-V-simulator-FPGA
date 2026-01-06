import chisel3._
import chisel3.util._

class Decoder extends Module {
  val io = IO(new Bundle {
    val instr   = Input(UInt(32.W))
    val rs1     = Output(UInt(5.W))
    val rs2     = Output(UInt(5.W))
    val rd      = Output(UInt(5.W))
    val aluOp   = Output(UInt(8.W))
    val imm     = Output(UInt(32.W))
  })

  io.rs1 := io.instr(19,15)
  io.rs2 := io.instr(24,20)
  io.rd  := io.instr(11,7)

  val opcode = io.instr(6,0)
  val funct3 = io.instr(14,12)
  val funct7 = io.instr(31,25)

  io.aluOp := 255.U
  io.imm   := 0.U

  // ADD
  when (opcode === "b0110011".U && funct3 === 0.U && funct7 === 0.U) {
    io.aluOp := 0.U
  }

  // ADDI
  when (opcode === "b0010011".U && funct3 === 0.U) {
    io.aluOp := 10.U
    io.imm   := io.instr(31,20).asSInt.asUInt
  }
}
