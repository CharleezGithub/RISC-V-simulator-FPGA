package empty

import chisel3._
import chisel3.util._
class UartBootloader extends Module {
    val io = IO(new Bundle {
        val start = Input(Bool())
        val done = Output(Bool())
        val rxValid = Input(Bool())
        val rxData = Input(UInt(8.W))
        val memWriteEn = Output(Bool())
        val memWriteAddr = Output(UInt(32.W))
        val memWriteData = Output(UInt(32.W)) // ← Changed to 32 bits
    })

    val wordAddr = RegInit(0.U(32.W))
    val byteIdx = RegInit(0.U(2.W))
    val byteBuffer = RegInit(VecInit(Seq.fill(4)(0.U(8.W))))
    val active = RegInit(false.B)
    val doneReg = RegInit(false.B)

    val wordsRemaining = RegInit(0.U(32.W))
    val headerPending = RegInit(true.B)

    val incomingWord = Wire(UInt(32.W))
    incomingWord := Cat(
        byteBuffer(3),
        byteBuffer(2),
        byteBuffer(1),
        byteBuffer(0)
    )
    when(active && io.rxValid && byteIdx === 3.U) {
        incomingWord := Cat(
            io.rxData,
            byteBuffer(2),
            byteBuffer(1),
            byteBuffer(0)
        )
    }

    io.memWriteEn := false.B
    io.memWriteAddr := wordAddr
    io.memWriteData := incomingWord
    io.done := doneReg

    when(io.start) {
        active := true.B
        wordAddr := 0.U
        byteIdx := 0.U
        doneReg := false.B
        headerPending := true.B
        wordsRemaining := 0.U
    }

    when(active && io.rxValid) {
        byteBuffer(byteIdx) := io.rxData
        when(byteIdx === 3.U) {
            when(headerPending) {
                wordsRemaining := incomingWord
                headerPending := false.B
                byteIdx := 0.U
                when(incomingWord === 0.U) {
                    doneReg := true.B
                    active := false.B
                }
            }.elsewhen(wordsRemaining =/= 0.U) {
                io.memWriteEn := true.B
                wordAddr := wordAddr + 1.U
                byteIdx := 0.U
                wordsRemaining := wordsRemaining - 1.U
                when(wordsRemaining === 1.U) {
                    doneReg := true.B
                    active := false.B
                }
            }.otherwise {
                byteIdx := 0.U
            }
        }.otherwise {
            byteIdx := byteIdx + 1.U
        }
    }
}
