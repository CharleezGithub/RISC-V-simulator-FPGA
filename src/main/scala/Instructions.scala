class Instructions extends Module {
  val io = IO(new Bundle {
    val instrNum = Input(UInt(8.W))
    val a        = Input(UInt(32.W))
    val b        = Input(UInt(32.W))
    val out      = Output(UInt(32.W))
  })

  io.out := 0.U

  switch(io.instrNum) {
    is(0.U) { io.out := io.a + io.b } // ADD
    is(1.U) { io.out := io.a - io.b } // SUB
  }
}


object InstructionsMain extends App {
  println("Generating the adder hardware")
  emitVerilog(new Instructions(), Array("--target-dir", "generated"))
}