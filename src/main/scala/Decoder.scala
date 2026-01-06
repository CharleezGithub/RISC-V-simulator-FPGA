import chisel3._
import chisel3.util._

class Decoder extends Module {
  val io = IO(new Bundle {
    val instr    = Input(UInt(32.W))
    val rs1      = Output(UInt(5.W))
    val rs2      = Output(UInt(5.W))
    val rd       = Output(UInt(5.W))
    val instrNum = Output(UInt(8.W))
  })

  io.rs1 := io.instr(19,15)
  io.rs2 := io.instr(24,20)
  io.rd  := io.instr(11,7)

  val opcode = io.instr(6,0)
  val funct3 = io.instr(14,12)
  val funct7 = io.instr(31,25)

  io.instrNum := 255.U // INVALID

  when (opcode === "b0110011".U) {
    when (funct3 === "b000".U && funct7 === "b0000000".U) { io.instrNum := 0.U } // add
    when (funct3 === "b000".U && funct7 === "b0100000".U) { io.instrNum := 1.U } // sub
  }
}
