import chisel3._
import chisel3.util._

class IFStage extends Module {
  val io = IO(new Bundle {
    val pcNext = Input(UInt(32.W))
    val pc     = Output(UInt(32.W))
    val instr  = Output(UInt(32.W))
  })

  val pcReg = RegInit(0.U(32.W))

  pcReg := io.pcNext

  io.pc := pcReg

  // TEMP: hardcoded instruction (addi x1, x0, 0x123)
  io.instr := "h12300093".U
}

