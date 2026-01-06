import chisel3._
import chisel3.util._

class IF_reg_ID() extends Module {
    val io = IO(new Bundle{
        val pcIn = Input(UInt(32.W))
        val instrIn = Input(UInt(32.W))

        val pcRegOut = Output(UInt(32.W))
        val instrRegOut = Output(UInt(32.W))
    })

    val pcReg = RegInit(0(32.W))
    val instrReg = RegInit(0(32.W))

    pcRegsaved = io.pcIn
    instrRegsaved = io.instrIn

    io.pcRegOut := pcReg
    io.instrRegOut := instrRegsaved
}

class ID_reg_EX extends Module {
  val io = IO(new Bundle {
    val rs1 = Input(UInt(5.W))
    val rs2 = Input(UInt(5.W))
    val rd  = Input(UInt(5.W))
    val rdData = Input(UInt(32.W))
    val we = Input(Bool())
    val instrNum = Input(UInt(32.W))

    val instrRegOut = Output(UInt(32.W))
    val rs1Data = Output(UInt(32.W))
    val rs2Data = Output(UInt(32.W))
  })

  val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
  val instrReg = RegInit(0.U(32.W))

  instrReg = io.instrNum

  io.rs1Data := Mux(io.rs1.orR, regs(io.rs1), 0.U)
  io.rs2Data := Mux(io.rs2.orR, regs(io.rs2), 0.U)
  io.instrRegOut := instrReg 
  when(io.we && io.rd.orR) {
    regs(io.rd) := io.rdData
  }
}
