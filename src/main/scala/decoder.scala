import chisel3._
import chisel3.util._

class Decode extends Module {
    val io = IO(new Bundle {
        val pcIn = Input(UInt(32.W))
        val instrIn = Input(UInt(32.W))

        //inputs for reg writes
        val writeData = Input(UInt(32.W))
        val writeFlag = Input(Bool())
        val writeAddr = Input(UInt(5.W))

        // Outputs
        val rs1Out = Output(UInt(32.W))
        val rs2Out = Output(UInt(32.W))
        val opcodeOut = Output(UInt(32.W))
        val funct3Out = Output(UInt(32.W))
        val funct7Out = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))
        val instrSel = Output(UInt(32.W)) 
    })

    val instr = io.instrIn

    // We split the instruction into its fields so we can make easy switch statement
    val opcode = instr(6,0)
    val rdAddr = instr(11,7)
    val funct3 = instr(14,12)
    val rs1 = instr(19,15)
    val rs2 = instr(24,20)
    val funct7 = instr(31,25)

    //32 register files
    val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
    //writing system for the 32 regs
    when(io.writeFlag === true.B){
      regs(io.writeAddr) := io.writeData
    }
    regs(0) := 0.U  

    //write-read-bypass yap (double check later) & passing values
    io.rs1Out := Mux(
      io.writeFlag && (io.writeAddr === rs1) && (io.writeAddr =/= 0.U),
      io.writeData,
      regs(rs1)
    )

    io.rs2Out := Mux(
      io.writeFlag && (io.writeAddr === rs1) && (io.writeAddr =/= 0.U),
      io.writeData,
      regs(rs1)
    )

    io.funct3Out := funct3
    io.funct7Out := funct7
    io.opcodeOut := opcode

    // we just forward the pc to the next stage nothing else neccesary
    io.pcOut := io.pcIn

  
}
object Decode extends App {
  emitVerilog(new Decode())
}