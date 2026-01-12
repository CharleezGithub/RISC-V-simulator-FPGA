import chisel3._
import chisel3.util._

class IF_ID extends Module {
    val io = IO(new Bundle {
        val pcIn = Input(UInt(32.W))
        val instrIn = Input(UInt(32.W))

        val pcOut = Output(UInt(32.W))
        val instr = Output(UInt(32.W))
    })
    // Registers
    val pcReg = RegInit(0.U(32.W))
    val instrReg = RegInit(0.U(32.W))

    // Connecting inputs to registers
    pcReg := io.pcIn
    instrReg := io.instrIn

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
        val rdaddrIn = Input(UInt(8.W))
        val rs1In = Input(UInt(8.W))
        val rs2In = Input(UInt(8.W))
        val pcIn = Input(UInt(32.W))

        val opcodeIn = Input(UInt(32.W))
        val funct3In = Input(UInt(32.W))
        val funct7In = Input(UInt(32.W))

        val immIIn = Input(UInt(32.W))
        val immSIn = Input(UInt(32.W))
        val immBIn = Input(UInt(32.W))
        val immUIn = Input(UInt(32.W))
        val immJIn = Input(UInt(32.W))

        // Outputs
        val rdaddrOut = Output(UInt(8.W))
        val rs1Out = Output(UInt(8.W))
        val rs2Out = Output(UInt(8.W))
        val pcOut = Output(UInt(32.W))

        val opcodeOut = Output(UInt(32.W))
        val funct3Out = Output(UInt(32.W))
        val funct7Out = Output(UInt(32.W))

        val immIOut = Output(UInt(32.W))
        val immSOut = Output(UInt(32.W))
        val immBOut = Output(UInt(32.W))
        val immUOut = Output(UInt(32.W))
        val immJOut = Output(UInt(32.W))


        //Control signals input for reg
        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool())

        //Control signals output for reg
        val widthSizeOut = Output(UInt(2.W))
        val memWriteOut = Output(Bool())
        val memReadOut = Output(Bool())
        val wbFlagOut = Output(Bool())
        val wbALUOrMemOut = Output(Bool())

    })
    // Pipeline Registers
    val rdaddrReg = RegInit(0.U(8.W))
    val rs1Reg = RegInit(0.U(8.W))
    val rs2Reg = RegInit(0.U(8.W))
    val pcInReg = RegInit(0.U((32.W)))

    val opcodeReg = RegInit(0.U((32.W)))
    val funct3Reg = RegInit(0.U((32.W)))
    val funct7Reg = RegInit(0.U((32.W)))

    val immIReg = RegInit(0.U((32.W)))
    val immSReg = RegInit(0.U((32.W)))
    val immBReg = RegInit(0.U((32.W)))
    val immUReg = RegInit(0.U((32.W)))
    val immJReg = RegInit(0.U((32.W)))

    // Connecting input to register
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


    // Regs and outputs for control signals
    val widthsizeReg = RegInit(UInt(2.W))
    val memWriteReg = RegInit(Bool())
    val memReadReg = RegInit(Bool())
    val wbFlagReg = RegInit(Bool())
    val wbALUorMemReg = RegInit(Bool())

    widthsizeReg := io.widthSizeIn
    memWriteReg := io.memReadIn
    memReadReg := io.memReadIn
    wbFlagReg := io.wbFlagIn
    wbALUorMemReg := io.wbALUOrMemIn

    io.widthSizeOut := widthsizeReg
    io.memWriteOut := memWriteReg
    io.memReadOut := memReadReg
    io.wbFlagOut := wbFlagReg
    io.wbALUOrMemOut := wbALUorMemReg
}

object ID_EX extends App {
    emitVerilog(new ID_EX())
}

class EX_MEM extends Module {
    val io = IO(new Bundle {
        val rdaddrIn = Input(UInt(8.W))
        val ALUIn = Input(UInt(32.W))
        val pcIn = Input(UInt(32.W))

        val rdaddrOut = Output(UInt(32.W))
        val ALUOut = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))


        //Control signals input for reg
        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool())

        //Control signals output for reg
        val widthSizeOut = Output(UInt(2.W))
        val memWriteOut = Output(Bool())
        val memReadOut = Output(Bool())
        val wbFlagOut = Output(Bool())
        val wbALUOrMemOut = Output(Bool())        
    })
    // Registers
    val rdaddrReg = RegInit(0.U(8.W))
    val ALUReg = RegInit(0.U(32.W))
    val pcReg = RegInit(0.U(32.W))

    // Connecting input to registers
    rdaddrReg := io.rdaddrIn
    pcReg := io.pcIn
    ALUReg := io.ALUIn

    // Connecting output to registers
    io.rdaddrOut := rdaddrReg
    io.ALUOut := ALUReg
    io.pcOut := pcReg

    // Regs and outputs for control signals
    val widthsizeReg = RegInit(UInt(2.W))
    val memWriteReg = RegInit(Bool())
    val memReadReg = RegInit(Bool())
    val wbFlagReg = RegInit(Bool())
    val wbALUorMemReg = RegInit(Bool())

    widthsizeReg := io.widthSizeIn
    memWriteReg := io.memReadIn
    memReadReg := io.memReadIn
    wbFlagReg := io.wbFlagIn
    wbALUorMemReg := io.wbALUOrMemIn

    io.widthSizeOut := widthsizeReg
    io.memWriteOut := memWriteReg
    io.memReadOut := memReadReg
    io.wbFlagOut := wbFlagReg
    io.wbALUOrMemOut := wbALUorMemReg
}
object EX_MEM extends App {
    emitVerilog(new EX_MEM())
}

class MEM_WB extends Module {
    val io = IO(new Bundle {
        // val mem_read = Input(UInt(32.W))
        // val mem_write = Input(UInt(32.W))
        val ALUIn = Input(UInt(32.W))
        val rdaddrIn = Input(UInt(8.W))

        val ALUOut = Output(UInt(32.W))
        val rdaddrOut = Output(UInt(8.W))


        //Control signals input for reg
        val widthSizeIn = Input(UInt(2.W))
        val memWriteIn = Input(Bool())
        val memReadIn = Input(Bool())
        val wbFlagIn = Input(Bool())
        val wbALUOrMemIn = Input(Bool())

        //Control signals output for reg
        val widthSizeOut = Output(UInt(2.W))
        val memWriteOut = Output(Bool())
        val memReadOut = Output(Bool())
        val wbFlagOut = Output(Bool())
        val wbALUOrMemOut = Output(Bool())        
    })
    // Registers
    val ALUReg = RegInit(0.U(32.W))
    val rdaddrReg = RegInit(0.U(8.W))

    // Connecting input to registers
    ALUReg := io.ALUIn
    rdaddrReg := io.rdaddrIn

    // Connecting output to registers
    io.ALUOut := ALUReg
    io.rdaddrOut := rdaddrReg
    
    // Regs and outputs for control signals
    val widthsizeReg = RegInit(UInt(2.W))
    val memWriteReg = RegInit(Bool())
    val memReadReg = RegInit(Bool())
    val wbFlagReg = RegInit(Bool())
    val wbALUorMemReg = RegInit(Bool())

    widthsizeReg := io.widthSizeIn
    memWriteReg := io.memReadIn
    memReadReg := io.memReadIn
    wbFlagReg := io.wbFlagIn
    wbALUorMemReg := io.wbALUOrMemIn

    io.widthSizeOut := widthsizeReg
    io.memWriteOut := memWriteReg
    io.memReadOut := memReadReg
    io.wbFlagOut := wbFlagReg
    io.wbALUOrMemOut := wbALUorMemReg
}
object MEM_WB extends App {
    emitVerilog(new MEM_WB())
}
