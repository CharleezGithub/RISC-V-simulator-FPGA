package empty

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class EndToEndCPUTest extends AnyFlatSpec with ChiselScalatestTester {

    "RISCV" should "work" in {
        test(new RISCV) { dut =>
            val programWords = Seq(
                "h00700313".U(32.W), // addi x6, x0, 7
                "h00500293".U(32.W), // addi x5, x0, 5
                "h006283B3".U(32.W) // add  x7, x5, x6
            )

            // Pad it to length of 64
            val program = RegInit(
                VecInit(
                    programWords ++ Seq.fill(64 - programWords.length)(
                        0.U(32.W)
                    )
                )
            )

            dut.instructionMemory.poke(program)

            dut.clock.step(10)

            dut.printRegsLatch(7).expect(13)
        }
    }
}
