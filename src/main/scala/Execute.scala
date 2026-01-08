import chisel3._
import chisel3.util._

class Execute extends Module {
    val io = IO(new Bundle{
        // Inputs for operation selection
        val funct3 = Input(UInt(32.W))
        val funct7 = Input(UInt(32.W))
        val opcode = Input(UInt(32.W))

        // Input data values
        val rs1Data = Input(UInt(32.W))
        val rs2Data = Input(UInt(32.W))
        val immIOut = Input(UInt(32.W))
        val immSOut = Input(UInt(32.W))
        val immBOut = Input(UInt(32.W))
        val immUOut = Input(UInt(32.W))
        val immJOut = Input(UInt(32.W))
        val pcIn = Input(UInt(32.W))

        // Outputs
        val ALUout = Output(UInt(32.W))
        val pcOut = Output(UInt(32.W))
    })

    // Passing on relevant values
    io.pcOut := io.pcIn
    io.ALUout := 0.U // Init value
    

    switch(io.opcode) {
        //---------------------------------------------(   R-type   )------------------------------------------------------
        is("b0110011".U) { 
            switch(io.funct7) {
                is("b0000000".U) { 
                    switch(io.funct3) {
                        is("b000".U) { io.ALUout := io.rs1Data + io.rs2Data }                                       // ADD
                        is("b111".U) { io.ALUout := io.rs1Data & io.rs2Data }                                       // AND
                        is("b110".U) { io.ALUout := io.rs1Data | io.rs2Data }                                       // OR
                        is("b100".U) { /* XOR */ }                                                                  // XOR
                        is("b001".U) { /* SLL */ }                                                                  // SLL
                        is("b101".U) { /* SRL */ }                                                                  // SRL
                        is("b010".U) { io.ALUout := (io.rs1Data.asSInt < io.rs2Data.asSInt).asUInt }                // SLT
                        is("b011".U) { /* SLTU */ }                                                                 // SLTU
                    }
                }
                is("b0100000".U) { 
                    switch(io.funct3) {
                        is("b000".U) { io.ALUout := io.rs1Data - io.rs2Data }                                       // SUB
                        is("b101".U) { /* SRA */ }                                                                  // SRA
                    }
                }
            }
        }

        //---------------------------------------------(   I-type   )------------------------------------------------------
        is("b0010011".U) {
            switch(io.funct3) {
                is("b000".U) { io.ALUout := io.rs1Data + io.imm }                                                   // ADDI
                is("b111".U) { io.ALUout := io.rs1Data & io.imm }                                                   // ANDI
                is("b110".U) { io.ALUout := io.rs1Data | io.imm }                                                   // ORI
                is("b100".U) { /* XORI */ }                                                                         // XORI
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
    }
}
  

object Execute extends App {
  emitVerilog(new Execute())
}