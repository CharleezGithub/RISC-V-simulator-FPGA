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


        //forwarding unit
        val rs1Addr = Input(UInt(5.W))
        val rs2Addr = Input(UInt(5.W))

        val exmem_rd  = Input(UInt(5.W))
        val exmem_alu = Input(UInt(32.W))
        val exmem_wb  = Input(Bool())

        val memwb_rd      = Input(UInt(5.W))
        val memwb_alu     = Input(UInt(32.W))
        val memwb_load    = Input(UInt(32.W))
        val memwb_memRead = Input(Bool())
        val memwb_wb      = Input(Bool())

    })


    //forwarding control -- 0: no forwarding needed, 1: forward from ex/mem stage, 2:mem/wb forward from mem/wb (2nd option is needed because of lw requires stalling)
    val forwardA = Wire(UInt(2.W))
    val forwardB = Wire(UInt(2.W))
    forwardA := 0.U
    forwardB := 0.U

    when(io.exmem_wb && io.exmem_rd =/= 0.U && io.exmem_rd === io.rs1Addr) {
        forwardA := 1.U
    }.elsewhen(io.memwb_wb && io.memwb_rd =/= 0.U && io.memwb_rd === io.rs1Addr) {
        forwardA := 2.U
    }

    when(io.exmem_wb && io.exmem_rd =/= 0.U && io.exmem_rd === io.rs2Addr) {
        forwardB := 1.U
    }.elsewhen(io.memwb_wb && io.memwb_rd =/= 0.U && io.memwb_rd === io.rs2Addr) {
        forwardB := 2.U
    }

  
    val wbValue = Mux(io.memwb_memRead, io.memwb_load, io.memwb_alu)

    // forwarded operands
     val rs1 = Wire(UInt(32.W))
     val rs2 = Wire(UInt(32.W))

     rs1 := io.rs1Data
     rs2 := io.rs2Data

     switch(forwardA) {
         is(1.U) { rs1 := io.exmem_alu }
         is(2.U) { rs1 := wbValue }
     }

     switch(forwardB) {
         is(1.U) { rs2 := io.exmem_alu }
         is(2.U) { rs2 := wbValue }
     }



    // Passing on relevant values 
    io.rdaddrOut := io.rdaddr
    io.rs2DataOut := rs2
    io.pcOut := io.pcIn
    io.ALUOut := 0.U // Init value
    io.branchTaken  := false.B
    io.branchTarget := 0.U
    io.flush        := false.B

    switch(io.opcode) {
        // ---------------------------------------------(   R-type   )------------------------------------------------------
        is("b0110011".U) {
            switch(io.funct7) {
                is("b0000000".U) {
                    switch(io.funct3) {
                        is("b000".U) { io.ALUOut := rs1 + rs2 }                                                     // ADD
                        is("b111".U) { io.ALUOut := rs1 & rs2 }                                                     // AND
                        is("b110".U) { io.ALUOut := rs1 | rs2 }                                                     // OR
                        is("b100".U) { io.ALUOut := rs1 ^ rs2 }                                                     // XOR
                        is("b001".U) { io.ALUOut := rs1 << 1.U }                                                    // SLL 
                        is("b101".U) { io.ALUOut := rs1 >> 1.U }                                                    // SRL
                        is("b010".U) { io.ALUOut := (rs1.asSInt < rs2.asSInt).asUInt }                              // SLT
                        is("b011".U) { /* SLTU */ }                                                                 // SLTU
                    }
                }
                is("b0100000".U) {
                    switch(io.funct3) {
                        is("b000".U) { io.ALUOut := rs1 - rs2 }                                                     // SUB
                        is("b101".U) { /* SRA */ }                                                                  // SRA
                    }
                }
            }
        }

        // ---------------------------------------------(   I-type   )------------------------------------------------------
        is("b0010011".U) {
            switch(io.funct3) {
                is("b000".U) { io.ALUOut := rs1 + io.immI }                                                         // ADDI
                is("b111".U) { io.ALUOut := rs1 & io.immI }                                                         // ANDI
                is("b110".U) { io.ALUOut := rs1 | io.immI }                                                         // ORI
                is("b100".U) { io.ALUOut := rs1 ^ io.immI }                                                         // XORI
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
                is("b000".U) { io.ALUOut := rs1 + io.immS }                                                         // SB
                is("b001".U) { io.ALUOut := rs1 + io.immS }                                                         // SH
                is("b010".U) { io.ALUOut := rs1 + io.immS }                                                         // SW
            }
        }

        // ---------------------------------------------(   Loads   )------------------------------------------------------
        is("b0000011".U) {
            switch(io.funct3) {
                is("b000".U) { io.ALUOut := rs1 + io.immI }                                                         // LB
                is("b001".U) { io.ALUOut := rs1 + io.immI }                                                         // LH
                is("b010".U) { io.ALUOut := rs1 + io.immI }                                                         // LW
            }
        }

        // ---------------------------------------------(   B-type   )------------------------------------------------------
        is("b1100011".U) {
        val taken = WireDefault(false.B)

        switch(io.funct3) {
            is("b000".U) { taken := rs1 === rs2 }                                                                   // BEQ
            is("b001".U) { taken := rs1 =/= rs2 }                                                                   // BNE
            is("b100".U) { taken := rs1.asSInt <  rs2.asSInt }                                                      // BLT
            is("b101".U) { taken := rs1.asSInt >= rs2.asSInt }                                                      // BGE
            is("b110".U) { taken := rs1 < rs2 }                                                                     // BLTU
            is("b111".U) { taken := rs1 >= rs2 }                                                                    // BGEU
        }

        // When branch is taken, we need to flush the instructions that were fetched
        // after the branch instruction (in IF/ID and ID/EX stages)
        io.branchTaken  := taken
        io.branchTarget := io.pcIn + io.immB
        io.flush        := taken
        }
        // For unconditional jumps, also set flush
        // JAL and JALR would go here if implemented

        // ---------------------------------------------(   U-type   )------------------------------------------------------
        is("b0110111".U) {
            io.ALUOut := io.immU                                                                                    // LUI
        }
        is("b0010111".U) { 
            io.ALUOut := io.pcIn + io.immU                                                                          // AUIPC
        }

        // ---------------------------------------------(   J-type   )------------------------------------------------------


    }
}

object Execute extends App {
    emitVerilog(new Execute())
}
