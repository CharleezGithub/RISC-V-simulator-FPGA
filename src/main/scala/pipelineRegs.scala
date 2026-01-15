package empty

import chisel3._
import chisel3.util._

class IF_ID extends Module {
    val io = IO(new Bundle {
        val pcIn = Input(UInt(32.W))
        val instrIn = Input(UInt(32.W))

        val pcOut = Output(UInt(32.W))
        val instr = Output(UInt(32.W))

        //inputs for control hazard
        val flushIn = Input(Bool())
    })
    // Registers
    val pcReg = RegInit(0.U(32.W))
    val instrReg = RegInit(0.U(32.W))

    // Connecting inputs to registers
    pcReg := io.pcIn
     when(io.flushIn) {
        instrReg := "h00000013".U   // NOP = addi x0, x0, 0
    }.otherwise {
        instrReg := io.instrIn
    }

    // Connecting outputs to registers
    io.pcOut := pcReg
    io.instr := instrReg
}
object IF_ID extends App {
    emitVerilog(new IF_ID())
}

class ID_EX extends Module {
    val io = IO(new Bundle {
        // Inputs
        val rdaddrIn = Input(UInt(5.W))
        val rs1In = Input(UInt(32.W))
        val rs2In = Input(UInt(32.W))
        val pcIn = Input(UInt(32.W))

        val opcodeIn = Input(UInt(32.W))
        val funct3In = Input(UInt(32.W))
        val funct7In = Input(UInt(32.W))

        // Immidiate generations
        val immIIn = Input(UInt(32.W))
        val immSIn = Input(UInt(32.W))
        val immBIn = Input(UInt(32.W))
        val immUIn = Input(UInt(32.W))
        val immJIn = Input(UInt(32.W))

        // Control signals input for reg
        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())

        // Outputs
        val rdaddrOut = Output(UInt(5.W))
        val rs1Out = Output(UInt(32.W))
        val rs2Out = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))

        val opcodeOut = Output(UInt(32.W))
        val funct3Out = Output(UInt(32.W))
        val funct7Out = Output(UInt(32.W))

        // Immidiate generations
        val immIOut = Output(UInt(32.W))
        val immSOut = Output(UInt(32.W))
        val immBOut = Output(UInt(32.W))
        val immUOut = Output(UInt(32.W))
        val immJOut = Output(UInt(32.W))

        // Control signals output for reg
        val widthSizeOut = Output(UInt(2.W))
        val memWriteOut = Output(Bool())
        val memReadOut = Output(Bool())
        val wbFlagOut = Output(Bool())

        //control hazard
        val flushIn = Input(Bool())

    })
    // Pipeline Registers
    val rdaddrReg = RegInit(0.U(5.W))
    val rs1Reg = RegInit(0.U(32.W))
    val rs2Reg = RegInit(0.U(32.W))
    val pcInReg = RegInit(0.U((32.W)))

    val opcodeReg = RegInit(0.U((32.W)))
    val funct3Reg = RegInit(0.U((32.W)))
    val funct7Reg = RegInit(0.U((32.W)))

    val immIReg = RegInit(0.U((32.W)))
    val immSReg = RegInit(0.U((32.W)))
    val immBReg = RegInit(0.U((32.W)))
    val immUReg = RegInit(0.U((32.W)))
    val immJReg = RegInit(0.U((32.W)))

    val widthsizeReg = RegInit(0.U(2.W))
    val memWriteReg = RegInit(false.B)
    val memReadReg = RegInit(false.B)
    val wbFlagReg = RegInit(false.B)

    // Connecting input to register

    when(!io.flushIn){
    rdaddrReg := io.rdaddrIn
    rs1Reg := io.rs1In
    rs2Reg := io.rs2In
    pcInReg := io.pcIn

    opcodeReg := io.opcodeIn
    funct3Reg := io.funct3In
    funct7Reg := io.funct7In

    immIReg := io.immIIn
    immSReg := io.immSIn
    immBReg := io.immBIn
    immUReg := io.immUIn
    immJReg := io.immJIn

    widthsizeReg := io.widthSizeIn
    memWriteReg := io.memWriteIn
    memReadReg := io.memReadIn
    wbFlagReg := io.wbFlagIn
    } .otherwise{
    rdaddrReg := 0.U
    rs1Reg := 0.U
    rs2Reg := 0.U
    opcodeReg := 0.U
    funct3Reg := 0.U
    funct7Reg := 0.U

    immIReg := 0.U
    immSReg := 0.U
    immBReg := 0.U
    immUReg := 0.U
    immJReg := 0.U
    widthsizeReg := 0.U
    memWriteReg  := false.B
    memReadReg   := false.B
    wbFlagReg    := false.B
    }

    // Connecting output to register
    io.rdaddrOut := rdaddrReg
    io.rs1Out := rs1Reg
    io.rs2Out := rs2Reg
    io.pcOut := pcInReg

    io.opcodeOut := opcodeReg
    io.funct3Out := funct3Reg
    io.funct7Out := funct7Reg

    io.immIOut := immIReg
    io.immSOut := immSReg
    io.immBOut := immBReg
    io.immUOut := immUReg
    io.immJOut := immJReg

    io.widthSizeOut := widthsizeReg
    io.memWriteOut := memWriteReg
    io.memReadOut := memReadReg
    io.wbFlagOut := wbFlagReg
}

object ID_EX extends App {
    emitVerilog(new ID_EX())
}

class EX_MEM extends Module {
    val io = IO(new Bundle {
        // Inputs
        val rdaddrIn = Input(UInt(5.W))
        val rs2DataIn = Input(UInt(32.W))
        val ALUIn = Input(UInt(32.W))
        val pcIn = Input(UInt(32.W))

        // Control signals
        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())

        // Outputs
        val rdaddrOut = Output(UInt(5.W))
        val rs2DataOut = Output(UInt(32.W))
        val ALUOut = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))

        // Control signals
        val widthSizeOut = Output(UInt(2.W))
        val memWriteOut = Output(Bool())
        val memReadOut = Output(Bool())
        val wbFlagOut = Output(Bool())
    })
    // Registers
    val rdaddrReg = RegInit(0.U(5.W))
    val rs2DataReg = RegInit(0.U(32.W))
    val ALUReg = RegInit(0.U(32.W))
    val pcReg = RegInit(0.U(32.W))
    
    val widthsizeReg = RegInit(0.U(2.W))
    val memWriteReg = RegInit(false.B)
    val memReadReg = RegInit(false.B)
    val wbFlagReg = RegInit(false.B)

    // Connecting input to registers
    rdaddrReg := io.rdaddrIn
    rs2DataReg := io.rs2DataIn
    pcReg := io.pcIn
    ALUReg := io.ALUIn

    widthsizeReg := io.widthSizeIn
    memWriteReg := io.memWriteIn
    memReadReg := io.memReadIn
    wbFlagReg := io.wbFlagIn

    // Connecting output to registers
    io.rdaddrOut := rdaddrReg
    io.rs2DataOut := rs2DataReg
    io.ALUOut := ALUReg
    io.pcOut := pcReg

    io.widthSizeOut := widthsizeReg
    io.memWriteOut := memWriteReg
    io.memReadOut := memReadReg
    io.wbFlagOut := wbFlagReg
}
object EX_MEM extends App {
    emitVerilog(new EX_MEM())
}

class MEM_WB extends Module {
    val io = IO(new Bundle {
        // Inputs
        val ALUIn = Input(UInt(32.W))
        val rdaddrIn = Input(UInt(5.W))
        val loadDataIn = Input(UInt(32.W))

        // Control signals
        val widthSizeIn = Input(UInt(2.W))
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())

        // Branch signals
        val branchTakenIn = Input(Bool())
        val branchTargetIn = Input(UInt(32.W))

        // Outputs
        val ALUOut = Output(UInt(32.W))
        val rdaddrOut = Output(UInt(5.W))
        val loadDataOut = Output(UInt(32.W))

        // Branch signals
        val branchTakenOut = Output(Bool())
        val branchTargetOut = Output(UInt(32.W))

        // Control signals
        val widthSizeOut = Output(UInt(2.W))
        val memReadOut = Output(Bool())
        val wbFlagOut = Output(Bool())
    })
    // Registers
    val ALUReg = RegInit(0.U(32.W))
    val rdaddrReg = RegInit(0.U(5.W))
    val loadDataReg = RegInit(0.U(32.W))
  
    val widthsizeReg = RegInit(0.U(2.W))
    val memReadReg = RegInit(false.B)
    val wbFlagReg = RegInit(false.B)

    val branchTakenReg = RegInit(false.B)
    val branchTargetReg = RegInit(0.U(32.W))
    

    // Connecting input to registers
    ALUReg := io.ALUIn
    rdaddrReg := io.rdaddrIn
    loadDataReg := io.loadDataIn

    widthsizeReg := io.widthSizeIn
    memReadReg := io.memReadIn
    wbFlagReg := io.wbFlagIn

    branchTakenReg := io.branchTakenIn
    branchTargetReg := io.branchTargetIn

    // Connecting output to registers
    io.ALUOut := ALUReg
    io.rdaddrOut := rdaddrReg
    io.loadDataOut := loadDataReg

    io.widthSizeOut := widthsizeReg
    io.memReadOut := memReadReg
    io.wbFlagOut := wbFlagReg

    io.branchTakenOut := branchTakenReg
    io.branchTargetOut := branchTargetReg
}
object MEM_WB extends App {
    emitVerilog(new MEM_WB())
}
