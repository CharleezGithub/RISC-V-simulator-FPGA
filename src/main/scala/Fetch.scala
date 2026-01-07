import chisel3._
import chisel3.util._

class Fetch extends Module {
    val io = IO(new Bundle {
        val program    = Input(Vec(1024, UInt(32.W))) // full program memory
        val pcOut      = Output(UInt(32.W))          // current PC to send to ID
        val instr      = Output(UInt(32.W))          // fetched instruction
    })
    
    // Control inputs from later stages (dummy values for now)
    val branchEn = RegInit(false.B)             // branch/jump taken
    val branchAddr = RegInit(0.U(32.W))           // branch target
    val flush      = RegInit(false.B)            // pipeline flush signal
    
    // PC register
    val pcReg = RegInit(0.U(32.W))

    // Default next PC = PC + 4 (sequential)
    val pcNext = Mux(branchEn, branchAddr, pcReg + 4.U)
    pcReg := Mux(flush, 0.U, pcNext) // here we let current PC increment unless flush is true

    // Output current PC
    io.pcOut := pcReg

    // Instruction fetch
    val instrIndex = pcReg >> 2
    // io.instr := io.program(instrIndex)

    // TEMP: hardcoded instruction (addi x1, x0, 0x123)
    io.instr := "h12300093".U
}

object Fetch extends App {
  emitVerilog(new Fetch())
}