import chisel3._
import chisel3.util._

class IF_ID extends Module {
  val io = IO(new Bundle {
    val pcIn    = Input(UInt(32.W))
    val instrIn= Input(UInt(32.W))

    val pcOut   = Output(UInt(32.W))
    val instrOut= Output(UInt(32.W))
  })

  val pcReg    = Reg(UInt(32.W))
  val instrReg= Reg(UInt(32.W))

  pcReg    := io.pcIn
  instrReg:= io.instrIn

  io.pcOut    := pcReg
  io.instrOut:= instrReg
}

class ID_EX extends Module {
  val io = IO(new Bundle {
    val aluOpIn = Input(UInt(8.W))
    val rs1ValIn = Input(UInt(32.W))
    val rs2ValIn = Input(UInt(32.W))
    val rdIn = Input(UInt(5.W))
    val immIn = Input(UInt(32.W))

    val aluOpOut = Output(UInt(8.W))
    val rs1ValOut = Output(UInt(32.W))
    val rs2ValOut = Output(UInt(32.W))
    val rdOut = Output(UInt(5.W))
    val immOut = Output(UInt(32.W))
  })

  io.aluOpOut := RegNext(io.aluOpIn)
  io.rs1ValOut:= RegNext(io.rs1ValIn)
  io.rs2ValOut:= RegNext(io.rs2ValIn)
  io.rdOut    := RegNext(io.rdIn)
  io.immOut   := RegNext(io.immIn)
}

class EX_MEM extends Module {
  val io = IO(new Bundle {
    val aluOutIn = Input(UInt(32.W))
    val rdIn = Input(UInt(5.W))

    val aluOutOut = Output(UInt(32.W))
    val rdOut = Output(UInt(5.W))
  })

  io.aluOutOut := RegNext(io.aluOutIn)
  io.rdOut := RegNext(io.rdIn)
}

class MEM_WB extends Module {
  val io = IO(new Bundle {
    val dataIn = Input(UInt(32.W))
    val rdIn = Input(UInt(5.W))

    val dataOut = Output(UInt(32.W))
    val rdOut = Output(UInt(5.W))
  })

  io.dataOut := RegNext(io.dataIn)
  io.rdOut := RegNext(io.rdIn)
}


