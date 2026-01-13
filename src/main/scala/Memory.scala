import chisel3._
import chisel3.util._

class Memory extends Module {
    val io = IO(new Bundle {
        // val mem_read = Input(Bool())
        // val mem_write = Input(Bool())
        // Inputs
        val ALUIn = Input(UInt(32.W))
        val rdaddrIn = Input(UInt(8.W))
        val rs2DataIn = Input(UInt(32.W))

        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool())

        // Outputs
        val ALUOut = Output(UInt(32.W))
        val rdaddrOut = Output(UInt(8.W))
        val memOut = Output(Vec(64, UInt(32.W))) // For UART printing
    })
    // passing values
    io.ALUOut := io.ALUIn
    io.rdaddrOut := io.rdaddrIn

    // Calculate word address
    val wordAddr = io.ALUIn(11, 2) // divide by 4 for word index
    val byteOffset = io.ALUIn(1, 0)

    // Making datamemory as register
    val mem = RegInit(VecInit(Seq.fill(64)(0.U(32.W)))) // Change to 1024 when done
    // Read old word for partial writes
    val oldWord = mem(wordAddr)

    // -----------------------------------------(   New-word generation   )--------------------------------------------------
    // We only do this when memwrite in is high meaning its a S-type
    val newWord = Wire(UInt(32.W))
    newWord := oldWord // default

    when(io.memWriteIn) {
        switch(io.widthSizeIn) { // byte, halfword, word
            is("b00".U) { newWord := (oldWord & ~(0xFF.U << (byteOffset * 8))) | ((io.rs2DataIn & 0xFF.U) << (byteOffset * 8)) }
            is("b01".U) { newWord := (oldWord & ~(0xFFFF.U << (byteOffset * 8))) | ((io.rs2DataIn & 0xFFFF.U) << (byteOffset * 8)) }
            is("b10".U) { newWord := io.rs2DataIn }
        }
        // Write back to memory
        mem(wordAddr) := newWord
    }

    // Always update memOut for visualization
    io.memOut := mem
    //-----------------------------------------------------------------------------------------------------------------------
}

object Memory extends App {
    emitVerilog(new Memory())
}
