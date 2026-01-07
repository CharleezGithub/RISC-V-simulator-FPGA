import chisel3._
import chisel3.util._

class Decode extends Module {
    val io = IO(new Bundle {
        val pcIn = Input(UInt(32.W))
        val instrIn = Input(UInt(32.W))

        // Outputs
        val rs1Out = Output(UInt(32.W))
        val rs2Out = Output(UInt(32.W))
        val isADDOut = Output(Bool())
        val pcOut = Output(UInt(32.W))
    })
    
    val instr = io.instrIn

    // We split the instruction into its fields so we can make easy switch statement
    val opcode = instr(6,0)
    val rdAddr = instr(11,7)
    val funct3 = instr(14,12)
    val rs1 = instr(19,15)
    val rs2 = instr(24,20)
    val funct7 = instr(31,25)

    //io connection
    io.rs1Out := rs1
    io.rs2Out := rs2

    // we just forward the pc to the next stage nothing else neccesary
    io.pcOut := io.pcIn

    // Big switch statement to determine the instruction type (WE ONLY DO ADD FOR NOW)
    val isADD = (instr(6,0) === "b0110011".U) && (instr(14,12) === "b000".U) && (instr(31,25) === "b0000000".U)

    io.isADDOut := isADD
}
object Decode extends App {
  emitVerilog(new Decode())
}