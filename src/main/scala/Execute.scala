package empty

import chisel3._
import chisel3.util._

class Execute extends Module {
    val io = IO(new Bundle {

        // Instruction inputs and pc
        val rdaddr = Input(UInt(5.W))
        val rs1Data = Input(UInt(32.W))
        val rs2Data = Input(UInt(32.W))
        val pcIn = Input(UInt(32.W))

        val funct3 = Input(UInt(32.W))
        val funct7 = Input(UInt(32.W))
        val opcode = Input(UInt(32.W))

        // Immidiate inputs
        val immI = Input(UInt(32.W))
        val immS = Input(UInt(32.W))
        val immB = Input(UInt(32.W))
        val immU = Input(UInt(32.W))
        val immJ = Input(UInt(32.W))

        // Outputs
        val rdaddrOut = Output(UInt(5.W))
        val rs2DataOut = Output(UInt(32.W))
        val ALUOut = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))

        // Branch and flush outputs
        val branchTaken = Output(Bool())
        val branchTarget = Output(UInt(32.W))
        val flush = Output(Bool())
    })

    // Passing on relevant values 
    io.rdaddrOut := io.rdaddr
    io.rs2DataOut := io.rs2Data
    io.pcOut := io.pcIn
    io.ALUOut := 0.U // Init value

    switch(io.opcode) {
        // ---------------------------------------------(   R-type   )------------------------------------------------------
        is("b0110011".U) {
            switch(io.funct7) {
                is("b0000000".U) {
                    switch(io.funct3) {
                        is("b000".U) { io.ALUOut := io.rs1Data + io.rs2Data }                                       // ADD
                        is("b111".U) { io.ALUOut := io.rs1Data & io.rs2Data }                                       // AND
                        is("b110".U) { io.ALUOut := io.rs1Data | io.rs2Data }                                       // OR
                        is("b100".U) { io.ALUOut := io.rs1Data ^ io.rs2Data }                                       // XOR
                        is("b001".U) { io.ALUOut := io.rs1Data << 1.U }                                             // SLL May be wrong
                        is("b101".U) { io.ALUOut := io.rs1Data >> 1.U }                                             // SRL May be wrong
                        is("b010".U) { io.ALUOut := (io.rs1Data.asSInt < io.rs2Data.asSInt).asUInt }                // SLT
                        is("b011".U) { /* SLTU */ }                                                                 // SLTU
                    }
                }
                is("b0100000".U) {
                    switch(io.funct3) {
                        is("b000".U) { io.ALUOut := io.rs1Data - io.rs2Data }                                       // SUB
                        is("b101".U) { /* SRA */ }                                                                  // SRA
                    }
                }
            }
        }

        // ---------------------------------------------(   I-type   )------------------------------------------------------
        is("b0010011".U) {
            switch(io.funct3) {
                is("b000".U) { io.ALUOut := io.rs1Data + io.immI }                                                  // ADDI
                is("b111".U) { io.ALUOut := io.rs1Data & io.immI }                                                  // ANDI
                is("b110".U) { io.ALUOut := io.rs1Data | io.immI }                                                  // ORI
                is("b100".U) { io.ALUOut := io.rs1Data ^ io.immI }                                                  // XORI
                is("b010".U) { /* SLTI */ }                                                                         // SLTI
                is("b011".U) { /* SLTIU */ }                                                                        // SLTIU
                is("b001".U) { /* SLLI */ }                                                                         // SLLI

                is("b101".U) {
                    switch(io.funct7) {
                        is("b0000000".U) { /* SRLI */ }                                                             // SRLI
                        is("b0100000".U) { /* SRAI */ }                                                             // SRAI
                    }
                }
            }
        }
    
        // ---------------------------------------------(   S-type   )------------------------------------------------------
        is("b0100011".U) {
            switch(io.funct3) {
                is("b000".U) { io.ALUOut := io.rs1Data + io.immS }                                                  // SB
                is("b001".U) { io.ALUOut := io.rs1Data + io.immS }                                                  // SH
                is("b010".U) { io.ALUOut := io.rs1Data + io.immS }                                                  // SW
            }
        }

        // ---------------------------------------------(   Loads   )------------------------------------------------------
        is("b0000011".U) {
            switch(io.funct3) {
                is("b000".U) { io.ALUOut := io.rs1Data + io.immI }                                                  // LB
                is("b001".U) { io.ALUOut := io.rs1Data + io.immI }                                                  // LH
                is("b010".U) { io.ALUOut := io.rs1Data + io.immI }                                                  // LW
            }
        }

        // ---------------------------------------------(   B-type   )------------------------------------------------------
        val taken = Wire(Bool())
        taken := false.B
        is("b1100011".U) {
            switch(io.funct3) {
                is("b000".U) { io.branchTaken := io.rs1Data === io.rs2Data }                                        // BEQ
                is("b001".U) { io.branchTaken := io.rs1Data =/= io.rs2Data }                                        // BNE
                is("b100".U) { io.branchTaken := io.rs1Data.asSInt < io.rs2Data.asSInt }                            // BLT
                is("b101".U) { io.branchTaken := io.rs1Data.asSInt >= io.rs2Data.asSInt }                           // BGE
                is("b110".U) { io.branchTaken := io.rs1Data < io.rs2Data }                                          // BLTU
                is("b111".U) { io.branchTaken := io.rs1Data >= io.rs2Data }                                         // BGEU
            }
            // Branching calculations
            io.branchTaken := taken
            io.branchTarget := io.pcIn + io.immB
            when(io.pcIn === io.pcIn + io.immB){
                taken := false.B
            }
            io.flush := taken   // only flush if branch is actually taken
        }
    }
}

object Execute extends App {
    emitVerilog(new Execute())
}
