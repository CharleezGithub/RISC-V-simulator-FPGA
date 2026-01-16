package empty

import chisel3._
import chisel3.util._

class Decode extends Module {
    val io = IO(new Bundle {
        val pcIn = Input(UInt(32.W))
        val instrIn = Input(UInt(32.W))

        // Control unit inputs
        val writeData = Input(UInt(32.W))
        val writeFlag = Input(Bool())
        val writeAddr = Input(UInt(5.W))

        // Outputs
        val rs1Out = Output(UInt(32.W))
        val rs2Out = Output(UInt(32.W))
        val opcodeOut = Output(UInt(32.W))
        val funct3Out = Output(UInt(32.W))
        val funct7Out = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))
        val immIOut = Output(UInt(32.W))
        val immSOut = Output(UInt(32.W))
        val immBOut = Output(UInt(32.W))
        val immUOut = Output(UInt(32.W))
        val immJOut = Output(UInt(32.W))
        val rdAddrOut = Output(UInt(5.W))

        // Control unit outputs
        val widthSizeOut = Output(UInt(2.W)) // selects mem access size (00=byte, 01=half, 10=word)
        val memWriteOut = Output(Bool()) // enables writ to data mem
        val memReadOut = Output(Bool()) // enables read from data mem
        val wbFlagOut = Output(Bool()) // enables writing a val to reg

        // Expose full register file for printing
        val regsOut = Output(Vec(32, UInt(32.W)))
    })

    val instr = io.instrIn

    // We split the instruction into its fields so we can make easy switch statement
    val opcode = instr(6, 0)
    val rdAddr = instr(11, 7)
    val funct3 = instr(14, 12)
    val rs1 = instr(19, 15)
    val rs2 = instr(24, 20)
    val funct7 = instr(31, 25)

    // immidate value generation
    val immI = Cat(Fill(20, instr(31)), instr(31, 20)) // I-type: sign-extended instr[31:20]
    val immS = Cat(Fill(20, instr(31)), instr(31, 25), instr(11, 7)) // S-type: sign-extended
    val immB = Cat(Fill(19, instr(31)), instr(31), instr(7), instr(30, 25), instr(11, 8), 0.U(1.W)) // B-type: sign-extended branch immediate
    val immU = Cat(instr(31, 12), 0.U(12.W)) // U-type: upper immediate
    val immJ = Cat(Fill(11, instr(31)), instr(31), instr(19, 12), instr(20), instr(30, 21), 0.U(1.W)) // J-type: sign-extended jump immediate

    // --------------------------------------------( Register file logic )--------------------------------------------------

    val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))

    // writing system for the 32 regs (signals from write-back)
    when(io.writeFlag === true.B) {
        regs(io.writeAddr) := io.writeData
    }
    regs(0) := 0.U // hardwire x0 to 0
    io.regsOut := regs // for UART printing

    // ---------------------------------------------------------------------------------------------------------------------

    // This will find the data on rs1 and rs2 addr in the register file and pass it on to next stage
    io.rs1Out := Mux(
        io.writeFlag && (io.writeAddr === rs1) && (io.writeAddr =/= 0.U),
        io.writeData,
        regs(rs1)
    )
    io.rs2Out := Mux(
        io.writeFlag && (io.writeAddr === rs2) && (io.writeAddr =/= 0.U),
        io.writeData,
        regs(rs2)
    )

    // ------------------------------------------------( Control Unit )-----------------------------------------------------
    // Defaults
    io.memReadOut := false.B
    io.memWriteOut := false.B
    io.wbFlagOut := false.B
    io.widthSizeOut := "b00".U

    switch(opcode) {
        // load word sets
        is("b0000011".U) {
            io.memReadOut := true.B
            io.memWriteOut := false.B
            io.wbFlagOut := true.B

            switch(funct3) {
                is("b000".U) { io.widthSizeOut := "b00".U } // LH
                is("b001".U) { io.widthSizeOut := "b01".U } // LW
                is("b010".U) { io.widthSizeOut := "b10".U } // WORD
                is("b100".U) { io.widthSizeOut := "b00".U } // LBU
                is("b101".U) { io.widthSizeOut := "b01".U } // LHU
            }
        }

        is("b0100011".U) {
            io.memReadOut := false.B
            io.memWriteOut := true.B
            io.wbFlagOut := false.B

            switch(funct3) {
                is("b000".U) { io.widthSizeOut := "b00".U } // SB
                is("b001".U) { io.widthSizeOut := "b01".U } // SH
                is("b010".U) { io.widthSizeOut := "b10".U } // SW
            }
        }
        // Jumps, ALU-I, ALu-reg,
        is("b0110011".U, "b0010011".U, "b1101111".U, "b1100111".U) {
            io.memReadOut := false.B
            io.memWriteOut := false.B
            io.wbFlagOut := true.B
        }
        // branch
        is("b1100011".U) {
            io.memReadOut := false.B
            io.memWriteOut := false.B
            io.wbFlagOut := false.B
        }
        // upper imm
        is("b0110111".U, "b0010111".U) {
            io.memReadOut := false.B
            io.memWriteOut := false.B
            io.wbFlagOut := true.B
        }
    }

    // Forward all relevant values to next module
    io.rdAddrOut := rdAddr
    io.funct3Out := funct3
    io.funct7Out := funct7
    io.opcodeOut := opcode

    io.pcOut := io.pcIn

    io.immBOut := immB
    io.immIOut := immI
    io.immJOut := immJ
    io.immSOut := immS
    io.immUOut := immU

}
object Decode extends App {
    emitVerilog(new Decode())
}
