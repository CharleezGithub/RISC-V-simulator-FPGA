package empty

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class EndToEndCPUTest extends AnyFlatSpec with ChiselScalatestTester {

    "RISCV" should "work" in {
        val programWords = Seq(
            "h00700313".U(32.W), // addi x6, x0, 7
            "h00500293".U(32.W), // addi x5, x0, 5
            "h00000013".U(32.W), // nop (addi x0, x0, 0) - avoid data hazard
            "h00000013".U(32.W), // nop (addi x0, x0, 0) - avoid data hazard
            "h006283B3".U(32.W) // add  x7, x5, x6
        )

        test(new RISCV(programInit = programWords)) { dut =>
            dut.io.rx.poke(false.B)
            dut.io.readyToReadProgram.poke(false.B)
            dut.io.printRegs.poke(false.B)
            dut.io.printMem.poke(false.B)

            // Pulse runProgram to start the pipeline
            dut.io.runProgram.poke(true.B)
            dut.clock.step(1)
            dut.io.runProgram.poke(false.B)

            dut.clock.step(12)

            dut.io.regsOut(7).expect(12.U)
        }
    }
}
