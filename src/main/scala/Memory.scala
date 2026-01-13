import chisel3._
import chisel3.util._

class Memory extends Module {
    val io = IO(new Bundle {
        // val mem_read = Input(Bool())
        // val mem_write = Input(Bool())
        // Inputs
        val ALUIn = Input(UInt(32.W))
        val rdaddrIn = Input(UInt(8.W))

        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool())

        // Outputs
        val ALUOut = Output(UInt(32.W))
        val rdaddrOut = Output(UInt(8.W))
    })
    // passing values 
    io.ALUOut := io.ALUIn
    io.rdaddrOut := io.rdaddrIn

    // Simple data memory 
    val mem = Mem(64, UInt(32.W)) // Change to 1024 when done





}
object Memory extends App {
    emitVerilog(new Memory())
}
