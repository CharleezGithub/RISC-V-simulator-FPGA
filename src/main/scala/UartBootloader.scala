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

    io.memWriteEn := false.B
    io.memWriteAddr := wordAddr
    io.memWriteData := Cat(
        byteBuffer(3),
        byteBuffer(2),
        byteBuffer(1),
        byteBuffer(0)
    )
    io.done := doneReg

    when(io.start) {
        active := true.B
        wordAddr := 0.U
        byteIdx := 0.U
        doneReg := false.B
    }

    when(io.rxData === 0xff.U && io.rxValid && active) {
        doneReg := true.B
        active := false.B
    }.elsewhen(active && io.rxValid) {
        byteBuffer(byteIdx) := io.rxData
        when(byteIdx === 3.U) {
            io.memWriteEn := true.B
            wordAddr := wordAddr + 1.U
            byteIdx := 0.U
        }.otherwise {
            byteIdx := byteIdx + 1.U
        }
    }
}
