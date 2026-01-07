import chisel3._
import chisel3.util._

class Execute extends Module {
    val io = IO(new Bundle{
        val rs1In = Input(UInt(32.W))
        val rs2In = Input(UInt(32.W))
        val isADDIn = Input(Bool())
        val pcIn = Input(UInt(32.W))

        val ALUout = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))
    })

    // Passing on relevant values
    io.pcOut := io.pcIn

    io.ALUout := RegInit(11111.U(32.W))
    
    when(io.isADDIn === true.B){
        io.ALUout := io.rs1In + io.rs2In
    } 
    
}
object Execute extends App {
  emitVerilog(new Execute())
}