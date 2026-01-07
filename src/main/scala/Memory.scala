import chisel3._
import chisel3.util._

class Memory extends Module{
    val io = IO(new Bundle{
        // val mem_read = Input(Bool())
        // val mem_write = Input(Bool())
        val ALUin = Input(UInt(32.W))

        val ALUout = Output(UInt(32.W)) 
    })

    io.ALUout := io.ALUin

}
object Memory extends App {
  emitVerilog(new Memory())
}