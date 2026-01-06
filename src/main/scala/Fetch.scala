import chisel3._
import chisel3.util._

class Fetch() extends Module {
    val io = IO(new Bundle {
        val pc = Output(UInt(32.W))
        val instr = Output(UInt(32.W))
    })

   val pcReg = RegInit(0.U(32.W))

   pcReg := pcReg + 4.U
    
   io.pc := pcReg
   //hardcoded instruction fra git 0x12300093
   io.instr := "h12300093".U(32.W)
}

