class ALU extends Module {
  val io = IO(new Bundle {
    val aluOp = Input(UInt(8.W))
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val out = Output(UInt(32.W))
  })

  io.out := 0.U

  switch(io.aluOp) {
    is(0.U)  { io.out := io.a + io.b }      // add
    is(10.U) { io.out := io.a + io.b }      // addi
  }
}

object ALUMain extends App {
  println("Generating the adder hardware")
  emitVerilog(new ALU(), Array("--target-dir", "generated"))
}