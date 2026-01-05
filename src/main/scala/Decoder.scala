import chisel3._
import chisel3.util._

class Decoder extends Module {
    val io = IO(new Bundle {
        val instr = Input(UInt(32.W))
        val rs1 = Output(UInt(5.W))
        val rs2 = Output(UInt(5.W))
        val rd  = Output(UInt(5.W))
        val instrNum = Output(UInt(8.W))
  })

    io.rs1 := io.instr(19,15)
    io.rs2 := io.instr(24,20)
    io.rd  := io.instr(11,7)

    // figuring out the instruction type
    val opcode = io.instr(6,0)
    val funct3 = io.instr(14,12)
    val funct7 = io.instr(31,25)

    // Default address
    io.instrNum := 255.U // INVALID

    // R-type (opcode 0110011)
    when (opcode === "b0110011".U) {
        when (funct3 === "b000".U && funct7 === "b0000000".U) { io.instrNum := 0.U } // add
        when (funct3 === "b000".U && funct7 === "b0100000".U) { io.instrNum := 1.U } // sub
        when (funct3 === "b111".U) { io.instrNum := 2.U } // and
        when (funct3 === "b110".U) { io.instrNum := 3.U } // or
        when (funct3 === "b100".U) { io.instrNum := 4.U } // xor
        when (funct3 === "b001".U) { io.instrNum := 5.U } // sll
        when (funct3 === "b101".U && funct7 === "b0100000".U) { io.instrNum := 6.U } // sra
        when (funct3 === "b101".U && funct7 === "b0000000".U) { io.instrNum := 7.U } // srl
        when (funct3 === "b010".U) { io.instrNum := 8.U } // slt
        when (funct3 === "b011".U) { io.instrNum := 9.U } // sltu
    }

    // I-type ALU (0010011)
    when (opcode === "b0010011".U) {
        when (funct3 === "b000".U) { io.instrNum := 10.U } // addi
        when (funct3 === "b111".U) { io.instrNum := 11.U } // andi
        when (funct3 === "b110".U) { io.instrNum := 12.U } // ori
        when (funct3 === "b100".U) { io.instrNum := 13.U } // xori
        when (funct3 === "b001".U) { io.instrNum := 14.U } // slli
        when (funct3 === "b101".U && funct7 === "b0100000".U) { io.instrNum := 15.U } // srai
        when (funct3 === "b101".U && funct7 === "b0000000".U) { io.instrNum := 16.U } // srli
        when (funct3 === "b010".U) { io.instrNum := 17.U } // slti
        when (funct3 === "b011".U) { io.instrNum := 18.U } // sltiu
    }

    // Branch (1100011)
    when (opcode === "b1100011".U) {
        when (funct3 === "b000".U) { io.instrNum := 19.U } // beq
        when (funct3 === "b001".U) { io.instrNum := 20.U } // bne
        when (funct3 === "b100".U) { io.instrNum := 21.U } // blt
        when (funct3 === "b101".U) { io.instrNum := 22.U } // bge
        when (funct3 === "b110".U) { io.instrNum := 23.U } // bltu
        when (funct3 === "b111".U) { io.instrNum := 24.U } // bgeu
    }

    // U-type
    when (opcode === "b0110111".U) { io.instrNum := 25.U } // lui
    when (opcode === "b0010111".U) { io.instrNum := 26.U } // auipc

    // Jumps
    when (opcode === "b1101111".U) { io.instrNum := 27.U } // jal
    when (opcode === "b1100111".U) { io.instrNum := 28.U } // jalr

     // Loads (0000011)
    when (opcode === "b0000011".U) {
        when (funct3 === "b000".U) { io.instrNum := 29.U } // lb
        when (funct3 === "b001".U) { io.instrNum := 30.U } // lh
        when (funct3 === "b010".U) { io.instrNum := 31.U } // lw
        when (funct3 === "b100".U) { io.instrNum := 32.U } // lbu
        when (funct3 === "b101".U) { io.instrNum := 33.U } // lhu
    }

    // Stores (0100011)
    when (opcode === "b0100011".U) {
        when (funct3 === "b000".U) { io.instrNum := 34.U } // sb
        when (funct3 === "b001".U) { io.instrNum := 35.U } // sh
        when (funct3 === "b010".U) { io.instrNum := 36.U } // sw
    }
}

