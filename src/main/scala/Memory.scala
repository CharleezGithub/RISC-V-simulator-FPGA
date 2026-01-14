package empty


import chisel3._
import chisel3.util._

class Memory extends Module {
    val io = IO(new Bundle {
        // Inputs
        val ALUIn = Input(UInt(32.W))
        val rdaddrIn = Input(UInt(5.W))
        val rs2DataIn = Input(UInt(32.W))

        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool())

        // Outputs
        val ALUOut = Output(UInt(32.W))
        val rdaddrOut = Output(UInt(5.W))
        val memOut = Output(Vec(64, UInt(32.W))) // For UART printing
        val loadDataOut = Output(UInt(32.W))
    })
    // passing values
    io.ALUOut := io.ALUIn
    io.rdaddrOut := io.rdaddrIn

    // Calculate word address
    val wordAddr = io.ALUIn(11, 2) // divide by 4 for word index
    val byteOffset = io.ALUIn(1, 0)

    // Making datamemory as register
    val mem = RegInit(VecInit(Seq.fill(64)(0.U(32.W)))) // Change to 1024 when done

    // -----------------------------------------(   New-word generation   )--------------------------------------------------
    // Read old word for partial writes
    val oldWord = mem(wordAddr)
    // We only do this when memwrite in is high meaning its a S-type
    val newWord = Wire(UInt(32.W))
    newWord := oldWord // default

    when(io.memWriteIn) {
        switch(io.widthSizeIn) { // byte, halfword, word
            is("b00".U) { newWord := (oldWord & ~(0xff.U << (byteOffset << 3))) | ((io.rs2DataIn & 0xff.U) << (byteOffset << 3)) }
            is("b01".U) { newWord := (oldWord & ~(0xffff.U << (byteOffset << 3))) | ((io.rs2DataIn & 0xffff.U) << (byteOffset << 3)) }
            is("b10".U) { newWord := io.rs2DataIn }
        }
        mem(wordAddr) := newWord

    }
    io.memOut := mem

    // ------------------------------------------(   Load-word fetching   )---------------------------------------------------
    // Read old word for partial writes
    val loadWord = mem(wordAddr)
    // We only do this when memRead in is high 
    val loadData = Wire(UInt(32.W))
    loadData := 0.U

    when(io.memReadIn) {
        switch(io.widthSizeIn) { // byte, halfword, word
            is("b00".U) { loadData := Cat(Fill(24, loadWord((byteOffset << 3) + 7)), loadWord((byteOffset << 3) + 7, (byteOffset << 3))) }
            is("b01".U) { loadData := Cat(Fill(16, loadWord((byteOffset << 3) + 15)), loadWord((byteOffset << 3) + 15, (byteOffset << 3))) }
            is("b10".U) { loadData := loadWord }
        }
    }
    io.loadDataOut := loadData
}

object Memory extends App {
    emitVerilog(new Memory())
}
