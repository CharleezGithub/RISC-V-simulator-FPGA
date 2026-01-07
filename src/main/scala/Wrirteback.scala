import chisel3._
import chisel3.util._

class Writeback extends Module {
    val io = IO(new Bundle{
        val ALUin = Input(UInt(32.W))

        val write_data = Output(UInt(32.W))
    })

    io.write_data := io.ALUin

}