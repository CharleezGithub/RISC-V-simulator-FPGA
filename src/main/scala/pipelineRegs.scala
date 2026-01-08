import chisel3._
import chisel3.util._

class IF_ID extends Module {
  val io = IO(new Bundle {
    val pcIn = Input(UInt(32.W))
    val instrIn = Input(UInt(32.W))

    val pcOut = Output(UInt(32.W))
    val instr = Output(UInt(32.W))
  })

  val pcReg = RegInit(0.U(32.W))
  val instrReg = RegInit(0.U(32.W))

  pcReg := io.pcIn
  instrReg := io.instrIn

  io.pcOut := pcReg
  io.instr := instrReg
}
object IF_ID extends App {
  emitVerilog(new IF_ID())
}

class ID_EX extends Module {
  val io = IO(new Bundle {
    // Inputs
    val rs1In = Input(UInt(8.W))
    val rs2In = Input(UInt(8.W))
    val pcIn = Input(UInt(32.W))

    val opcodeIn = Input(UInt(32.W))
    val funct3In = Input(UInt(32.W))
    val funct7In = Input(UInt(32.W))

    val immIIn = Input(UInt(32.W))
    val immSIn = Input(UInt(32.W))
    val immBIn = Input(UInt(32.W))
    val immUIn = Input(UInt(32.W))
    val immJIn = Input(UInt(32.W))

    // Outputs
    val rs1Out = Output(UInt(8.W))
    val rs2Out = Output(UInt(8.W))
    val pcOut = Output(UInt(32.W))

    val opcodeOut = Output(UInt(32.W))
    val funct3Out = Output(UInt(32.W))
    val funct7Out = Output(UInt(32.W))

    val immIOut = Output(UInt(32.W))
    val immSOut = Output(UInt(32.W))
    val immBOut = Output(UInt(32.W))
    val immUOut = Output(UInt(32.W))
    val immJOut = Output(UInt(32.W))

  })
  // Pipeline Registers
  val rs1Reg = RegInit(0.U(8.W))
  val rs2Reg = RegInit(0.U(8.W))
  val pcInReg = RegInit(0.U((32.W)))

  val opcodeReg = RegInit(0.U((32.W)))
  val funct3Reg = RegInit(0.U((32.W)))
  val funct7Reg = RegInit(0.U((32.W)))

  val immIReg = RegInit(0.U((32.W)))
  val immSReg = RegInit(0.U((32.W)))
  val immBReg = RegInit(0.U((32.W)))
  val immUReg = RegInit(0.U((32.W)))
  val immJReg = RegInit(0.U((32.W)))

  // Connecting input to register
  rs1Reg := io.rs1In
  rs2Reg := io.rs2In
  pcInReg := io.pcIn

  opcodeReg := io.opcodeIn
  funct3Reg := io.funct3In
  funct7Reg := io.funct7In

  immIReg := io.immIIn
  immSReg := io.immSIn
  immBReg := io.immBIn
  immUReg := io.immUIn
  immJReg := io.immJIn

  // Connecting output to register
  io.rs1Out := rs1Reg
  io.rs2Out := rs2Reg
  io.pcOut := pcInReg

  io.opcodeOut := opcodeReg
  io.funct3Out := funct3Reg
  io.funct7Out := funct7Reg

  io.immIOut := immIReg
  io.immSOut := immSReg
  io.immBOut := immBReg
  io.immUOut := immUReg
  io.immJOut := immJReg
}

object ID_EX extends App {
  emitVerilog(new ID_EX())
}

class EX_MEM extends Module {
  val io = IO(new Bundle {
    val ALUin = Input(UInt(32.W))
    val pcIn = Input(UInt(32.W))

    val ALUout = Output(UInt(32.W))
    val pcOut = Output(UInt(32.W))
  })

  val ALUReg = RegInit(0.U(32.W))
  val pcReg = RegInit(0.U(32.W))

  pcReg := io.pcIn
  ALUReg := io.ALUin

  io.ALUout := ALUReg
  io.pcOut := pcReg
}
object EX_MEM extends App {
  emitVerilog(new EX_MEM())
}

class MEM_WB extends Module {
  val io = IO(new Bundle {
    // val mem_read = Input(UInt(32.W))
    // val mem_write = Input(UInt(32.W))
    val ALUin = Input(UInt(32.W))

    val ALUout = Output(UInt(32.W))
  })

  val ALUReg = RegInit(0.U(32.W))
  ALUReg := io.ALUin

  io.ALUout := ALUReg
}
object MEM_WB extends App {
  emitVerilog(new MEM_WB())
}
