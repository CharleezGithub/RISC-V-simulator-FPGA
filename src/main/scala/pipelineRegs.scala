import chisel3._
import chisel3.util._

class IF_ID extends Module {
  new io = IO(new Bundle{
    val pcIn = Input(UInt(32.W))
    val instrIn = Input(UInt(32.W))
    
    val pcOut = Output(UInt(32.W))
    val instr = Output(UInt(32.W))
  })

  val pcReg = RegInit(UInt(0.U(32.W)))
  val instrReg = RegInit(UInt(0.U(32.W)))

  pcReg = io.pcIn
  instrReg = io.instrIn

  io.pcOut := pcReg
  io.instr := instrReg
}

class ID_reg_EX extends Module {
  new io = IO(new Bundle{
    
  })


}