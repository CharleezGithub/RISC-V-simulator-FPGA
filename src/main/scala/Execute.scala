import chisel3._
import chisel3.util._

class Execute extends Module {
    val io = IO(new Bundle{
        val rs1In = Input(UInt(8.W))
        val rs2In = Input(UInt(8.W))
        val isADDIn = Input(Bool())
        val pcIn = Input(UInt(32.W))

        val ALUout = Output(UInt(32.W))
    })

    when(isADDIn === true.B){
        io.ALUout := io.rs1In.U + io.rs2In.U
    } .otherwise{
        io.ALUout := 11111111.U
    }
}