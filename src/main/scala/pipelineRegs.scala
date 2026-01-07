import chisel3._
import chisel3.util._

class IF_ID extends Module {
  val io = IO(new Bundle{
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
  val io = IO(new Bundle{
        val rs1In = Input(UInt(8.W))
        val rs2In = Input(UInt(8.W))
        val isADDIn = Input(Bool())
        val pcIn = Input(UInt(32.W))

        val rs1Out = Output(UInt(8.W))
        val rs2Out = Output(UInt(8.W))
        val isADDOut = Output(Bool())
        val pcOut = Output(UInt(32.W))
  })

    val rs1Reg = RegInit(0.U(8.W))
    val rs2Reg = RegInit(0.U(8.W))
    val isAddReg = RegInit(false.B)
    val pcInReg = RegInit(0.U((32.W)))

    rs1Reg := io.rs1In
    rs2Reg := io.rs2In
    isAddReg := io.isADDIn
    pcInReg := io.pcIn

    io.rs1Out := rs1Reg
    io.rs2Out := rs2Reg
    io.isADDOut := isAddReg
    io.pcOut := pcInReg
}
object ID_EX extends App {
  emitVerilog(new ID_EX())
}

class EX_MEM extends Module {
  val io = IO(new Bundle{
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
  io.pcOut  := pcReg
}
object EX_MEM extends App {
  emitVerilog(new EX_MEM())
}

class MEM_WB extends Module {
  val io = IO(new Bundle{
    //val mem_read = Input(UInt(32.W))
    //val mem_write = Input(UInt(32.W))
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