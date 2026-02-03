package empty

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class fetchTest extends AnyFlatSpec with ChiselScalatestTester {

  "Fetch" should "fetch instructions" in {

    test(new Fetch) { dut =>

      val program = Seq(
        "h00100093".U, // PC = 0x80000000 -> addi x1, x0, 1
        "h00200113".U, // PC = 0x80000004 -> addi x2, x0, 2
        "h00300193".U  // PC = 0x80000008 -> addi x3, x0, 3
      )

      // Load program memory
      for (i <- program.indices) {
        dut.io.program(i).poke(program(i))
      }

      // --------------------
      // Reset cycle
      // --------------------
      dut.io.enable.poke(false.B)
      dut.io.resetPC.poke(true.B)
      dut.clock.step(1)

      dut.io.pcOut.expect("h80000000".U)
      dut.io.instr.expect("h00100093".U)


      dut.io.resetPC.poke(false.B)
      dut.io.enable.poke(true.B)

      dut.clock.step(1)
      dut.io.pcOut.expect("h80000004".U)
      dut.io.instr.expect("h00200113".U)

      dut.clock.step(1)
      dut.io.pcOut.expect("h80000008".U)
      dut.io.instr.expect("h00300193".U)

      dut.io.pcOut.expect("h80000008".U)
      dut.io.instr.expect("h00300193".U)
    }
  }
}
