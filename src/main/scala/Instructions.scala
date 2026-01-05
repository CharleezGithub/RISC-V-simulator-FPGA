import chisel3._
import chisel3.util._

class Instructions extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(8.W))  // instruction address
    val a    = Input(UInt(32.W))
    val b    = Input(UInt(32.W))
    val out  = Output(UInt(32.W))
  })

  val reg = Wire(UInt(32.W))
  reg := 0.U

  // -----------------------------
  // Instruction definitions
  // -----------------------------
  switch(io.addr) {
    // R-type instructions
    is(0.U) { reg := io.a + io.b }   // add
    is(1.U) { reg := io.a - io.b }   // sub
    is(2.U) { reg := io.a & io.b }   // and
    is(3.U) { reg := io.a | io.b }   // or
    is(4.U) { reg := io.a ^ io.b }   // xor
    is(5.U) { reg := io.a << io.b(4,0) } // sll
    is(6.U) { reg := (io.a.asSInt >> io.b(4,0)).asUInt } // sra
    is(7.U) { reg := io.a >> io.b(4,0) } // srl
    is(8.U) { reg := Mux(io.a.asSInt < io.b.asSInt, 1.U, 0.U) } // slt
    is(9.U) { reg := Mux(io.a < io.b, 1.U, 0.U) }               // sltu

    // I-type instructions
    is(10.U) { reg := io.a + io.b } // addi (using b as immediate)
    is(11.U) { reg := io.a & io.b } // andi
    is(12.U) { reg := io.a | io.b } // ori
    is(13.U) { reg := io.a ^ io.b } // xori
    is(14.U) { reg := io.a << io.b(4,0) } // slli
    is(15.U) { reg := (io.a.asSInt >> io.b(4,0)).asUInt }  // srai
    is(16.U) { reg := io.a >> io.b(4,0) } // srli
    is(17.U) { reg := Mux(io.a.asSInt < io.b.asSInt, 1.U, 0.U) } // slti
    is(18.U) { reg := Mux(io.a < io.b, 1.U, 0.U) }               // sltiu

    // Branch instructions (B-type)
    is(19.U) { reg := Mux(io.a === io.b, 1.U, 0.U) }   // beq
    is(20.U) { reg := Mux(io.a =/= io.b, 1.U, 0.U) }   // bne
    is(21.U) { reg := Mux(io.a.asSInt < io.b.asSInt, 1.U, 0.U) } // blt
    is(22.U) { reg := Mux(io.a.asSInt >= io.b.asSInt, 1.U, 0.U) } // bge
    is(23.U) { reg := Mux(io.a < io.b, 1.U, 0.U) }     // bltu
    is(24.U) { reg := Mux(io.a >= io.b, 1.U, 0.U) }    // bgeu

    // U-type instructions
    is(25.U) { reg := io.b << 12 } // lui
    is(26.U) { reg := io.a + (io.b << 12) } // auipc

    // J-type
    is(27.U) { reg := io.a + io.b } // jal (jump target)
    is(28.U) { reg := io.a + io.b } // jalr (jump target)
    
    // Loads (simplified)
    is(29.U) { reg := io.b } // lb
    is(30.U) { reg := io.b } // lh
    is(31.U) { reg := io.b } // lw
    is(32.U) { reg := io.b } // lbu
    is(33.U) { reg := io.b } // lhu

    // Stores (simplified)
    is(34.U) { reg := io.a } // sb
    is(35.U) { reg := io.a } // sh
    is(36.U) { reg := io.a } // sw
  }

  // Connecting io ports 
  io.out := reg
}

object InstructionsMain extends App {
  println("Generating the adder hardware")
  emitVerilog(new Instructions(), Array("--target-dir", "generated"))
}