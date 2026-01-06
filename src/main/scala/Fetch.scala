import chisel3._
import chisel3.util._

class IFStage extends Module {
    val io = IO(new Bundle {
        val program    = Input(Vec(1024, UInt(32.W))) // full program memory
        val pcOut      = Output(UInt(32.W))          // current PC to send to ID
        val instr      = Output(UInt(32.W))          // fetched instruction

        // Control inputs from later stages 
        val branchEn   = Input(Bool())               // branch/jump taken
        val branchAddr = Input(UInt(32.W))           // branch target
        val flush      = Input(Bool())               // pipeline flush signal
    })

    // PC register
    val pcReg = RegInit(0.U(32.W))

    // Default next PC = PC + 4 (sequential)
    val pcNext = Mux(io.branchEn, io.branchAddr, pcReg + 4.U)
    pcReg := Mux(io.flush, 0.U, pcNext) // here we let current PC increment unless flush is true

    // Output current PC
    io.pcOut := pcReg

    // Instruction fetch
    val instrIndex = pcReg >> 2
    io.instr := io.program(instrIndex)

    // TEMP: hardcoded instruction (addi x1, x0, 0x123)
    io.instr := "h12300093".U
}

