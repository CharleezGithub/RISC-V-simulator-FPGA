import chisel3._
import chisel3.util._

class Writeback extends Module {
    val io = IO(new Bundle {
        val ALUIn = Input(UInt(32.W))

        val rdAddr = Input(UInt(5.W))
        val rdDataIn = Input(UInt(32.W))
        val regWrite = Input(Bool())

        // To register file
        val rfWAddr = Output(UInt(5.W))
        val rfWData = Output(UInt(32.W))
        val rfWEn = Output(Bool())
    })

    io.rfData := io.ALUin
    io.rfWAddr := io.rdAddr
    
}

object Writeback extends App {
    emitVerilog(new Writeback())
}
