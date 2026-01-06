import chisel3._
import chisel3.util._

class RegFile extends Module {
  val io = IO(new Bundle {
    val rs1 = Input(UInt(5.W))
    val rs2 = Input(UInt(5.W))
    val rd  = Input(UInt(5.W))
    val rdData = Input(UInt(32.W))
    val we = Input(Bool())

    val rs1Data = Output(UInt(32.W))
    val rs2Data = Output(UInt(32.W))
  })

  val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))

  io.rs1Data := Mux(io.rs1.orR, regs(io.rs1), 0.U)
  io.rs2Data := Mux(io.rs2.orR, regs(io.rs2), 0.U)

  when (io.we && io.rd.orR) {
    regs(io.rd) := io.rdData
  }
}
