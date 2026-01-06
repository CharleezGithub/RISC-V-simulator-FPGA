import chisel3._
import chisel3.util._

class IF_reg_ID extends Module {
  new io = IO(new Bundle{
    val pc = Input(UInt(32.W))
    val instr = Input(UInt(32.W))
    
    val pcRegOut = Output(UInt(32.W))
    val instrRegOut = Output(UInt(32.W))
  })

  val pcReg = RegInit(UInt(0.U(32.W)))
  val instrReg = RegInit(UInt(0.U(32.W)))

  pcReg = io.pc
  instrReg = io.instr

  io.pcRegOut := pcReg
  io.instrRegOut := instrReg
}

class ID_reg_EX extends Module {
  new io = IO(new Bundle{
    
  })


}