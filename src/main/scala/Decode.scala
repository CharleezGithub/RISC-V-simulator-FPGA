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
        val rdAddrOut = Output(UInt(8.W))

        //Control unit outputs
        val widthSizeOut = Output(UInt(2.W)) // selects mem access size (00=byte, 01=half, 10=word)
        val memWriteOut = Output(Bool()) // enables writ to data mem
        val memReadOut = Output(Bool()) // enables read from data mem
        val wbFlagOut = Output(Bool()) // enables writing a val to reg
        val wbALUOrMemOut = Output(Bool())  // selects where to write back, whether to a reg (ALU = 0) or to mem (MEM = 1)
        
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
    val immI = Cat(funct7, rs2) // I-type: instr[31:20]
    val immS = Cat(funct7, rdAddr) // S-type: instr[31:25] ++ instr[11:7]
    val immB = Cat(funct7(6), rdAddr(0), funct7(5, 0), rdAddr(4, 1), 0.U) // B-type: branch immediate
    val immU = Cat(funct7, rs2, rs1, funct3, 0.U(12.W)) // U-type: upper immediate
    val immJ = Cat(funct7(6), rs1, funct3, rs2(0), funct7(5, 0), rs2(4, 1), 0.U) // J-type: jump immediate

    // 32 register file intialization
    val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
    // writing system for the 32 regs
    when(io.writeFlag === true.B) {
        regs(io.writeAddr) := io.writeData
    }
    regs(0) := 0.U // hardwire x0 to 0

    // write-read-bypass yap (double check later) & passing values
    io.rs1Out := Mux(
        io.writeFlag && (io.writeAddr === rs1) && (io.writeAddr =/= 0.U),
        io.writeData,
        regs(rs1)
    )
    io.rs2Out := Mux(
        io.writeFlag && (io.writeAddr === rs1) && (io.writeAddr =/= 0.U),
        io.writeData,
        regs(rs1)
    )
    
    // Control unit
    io.memReadOut := false.B
    io.memWriteOut := false.B
    io.wbFlagOut := false.B
    io.wbALUOrMemOut := false.B
    io.widthSizeOut := "b00".U
    switch(opcode){
        //load word sets
        is( "b0000011".U){
            switch(funct3){
                //LB
                is("b000".U){
                    io.memReadOut := true.B
                    io.memWriteOut := false.B
                    io.wbFlagOut := true.B
                    io.wbALUOrMemOut := true.B
                    io.widthSizeOut := "b0".U
                }
                //LH
                is("b001".U){
                    io.memReadOut := true.B
                    io.memWriteOut := false.B
                    io.wbFlagOut := true.B
                    io.wbALUOrMemOut := true.B
                    io.widthSizeOut := "b01".U
                }
                //LW
                is("b010".U){
                    io.memReadOut := true.B
                    io.memWriteOut := false.B
                    io.wbFlagOut := true.B
                    io.wbALUOrMemOut := true.B
                    io.widthSizeOut := "b00".U
                }
                //LBU
                is("b100".U){
                    io.memReadOut := true.B
                    io.memWriteOut := false.B
                    io.wbFlagOut := true.B
                    io.wbALUOrMemOut := true.B
                    io.widthSizeOut := "b00".U                    
                }
                //LHU
                is("b101".U){
                    io.memReadOut := true.B
                    io.memWriteOut := false.B
                    io.wbFlagOut := true.B
                    io.wbALUOrMemOut := true.B
                    io.widthSizeOut :=  "b01".U
                }
            }
        }
        is("b0100011".U){
            switch(funct3){
                //SB
                is("b000".U){
                    io.memReadOut := false.B
                    io.memWriteOut := true.B
                    io.wbFlagOut := false.B
                    io.wbALUOrMemOut := false.B
                    io.widthSizeOut := "b00".U
                }
                //SH
                is("b001".U){
                    io.memReadOut := false.B
                    io.memWriteOut := true.B
                    io.wbFlagOut := false.B
                    io.wbALUOrMemOut := false.B
                    io.widthSizeOut := "b01".U
                }
                //SW
                is("b010".U){
                    io.memReadOut := false.B
                    io.memWriteOut := true.B
                    io.wbFlagOut := false.B
                    io.wbALUOrMemOut := false.B
                    io.widthSizeOut := "b10".U
                }
            }
        }
        //Jumps, ALU-I, ALu-reg, 
        is("b0110011".U, "b0010011".U, "b1101111".U, "b1100111".U){
                io.memReadOut := false.B
                io.memWriteOut := false.B
                io.wbFlagOut := true.B
                io.wbALUOrMemOut := false.B   
        }
        //branch
        is("b1100011".U){
                io.memReadOut := false.B
                io.memWriteOut := false.B
                io.wbFlagOut := false.B
                io.wbALUOrMemOut := false.B               
        }
        //upper imm
        is("b0110111".U, "b0010111".U){
            io.memReadOut := false.B
            io.memWriteOut := false.B
            io.wbFlagOut := true.B
            io.wbALUOrMemOut := false.B   
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
