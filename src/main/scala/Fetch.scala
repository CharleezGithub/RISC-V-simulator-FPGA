package empty

import chisel3._
import chisel3.util._

class Fetch extends Module {
    val io = IO(new Bundle {
        val program = Input(Vec(64, UInt(32.W))) // full program memory
        val enable = Input(Bool())
        val resetPC = Input(Bool())
        val pcOut = Output(UInt(32.W)) // current PC to send to ID
        val instr = Output(UInt(32.W)) // fetched instruction

        val branchTaken = Input(Bool())
        val branchTarget = Input(UInt(32.W))

        val ecallOut = Output(Bool())
    })

    // Control inputs from later stages (dummy values for now)
    val branchEn = RegInit(false.B) // branch/jump taken
    val branchAddr = RegInit(0.U(32.W)) // branch target
    val flush = RegInit(false.B) // pipeline flush signal

    // PC register
    val pcReg = RegInit(0.U(32.W))

    // Default next PC = PC + 4 (sequential)
    val pcNext = Mux(io.branchTaken, io.branchTarget, pcReg + 4.U)

    // check if its ecall to break the code
    val ecall = io.instr === "h00000073".U  

    when(io.resetPC) {
        pcReg := 0.U
    }
    .elsewhen(io.enable && !ecall) {
        pcReg := pcNext // here we let current PC increment unless flush is true
    }

    // Output current PC
    io.pcOut := pcReg

    // Instruction fetch
    val instrIndex = pcReg >> 2
    io.instr := io.program(instrIndex)

    io.ecallOut := ecall
}

object Fetch extends App {
    emitVerilog(new Fetch())
}
