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
    })
    // passing values 
    io.ALUOut := io.ALUIn
    io.rdaddrOut := io.rdaddrIn

    // Calculate word address
    val wordAddr = io.ALUIn(11,2) // divide by 4 for word index
    val byteOffset = io.ALUIn(1,0)

    // Simple data memory 
    val mem = Mem(64, UInt(32.W)) // Change to 1024 when done
    // Read old word for partial writes
    val oldWord = mem.read(wordAddr)


    // -----------------------------------------(   New-word generation   )--------------------------------------------------
    // We only do this when memwrite in is high meaning its a S-type
    val newWord = Wire(UInt(32.W))
    newWord := oldWord // default

    when(io.memWriteIn) {
        switch(io.widthSizeIn) { //byte, hw, w
            is("b00".U) { newWord := (oldWord & ~(0xFF.U << (byteOffset*8))) | ((io.ALUIn & 0xFF.U) << (byteOffset*8)) } 
            is("b01".U) { newWord := (oldWord & ~(0xFFFF.U << (byteOffset*8))) | ((io.ALUIn & 0xFFFF.U) << (byteOffset*8)) }
            is("b10".U) { newWord := io.ALUIn }
    }
    mem.write(wordAddr, newWord)
  }
}

object Memory extends App {
    emitVerilog(new Memory())
}
