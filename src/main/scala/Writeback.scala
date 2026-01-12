import chisel3._
import chisel3.util._

class Writeback extends Module {
    val io = IO(new Bundle {
        // Inputs
        val ALUIn = Input(UInt(32.W))
        val rdAddr = Input(UInt(5.W))
        val rdDataIn = Input(UInt(32.W))

        // To register file
        val rfWAddr = Output(UInt(5.W))
        val rfWData = Output(UInt(32.W))

        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool()) // rfEnable

        // Output to reg file
        val rfWEn = Output(Bool())
    })
    // Data will be sent to certain address in register file when enable signal true
    io.rfData := io.ALUin
    io.rfWAddr := io.rdAddr

    // If we need to save ALU output to register file rfWEn is true
    io.rfWEn := Mux(io.wbALUOrMemIn, false.B, true.B)

}

object Writeback extends App {
    emitVerilog(new Writeback())
}
