package empty

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class decodeTest extends AnyFlatSpec with ChiselScalatestTester {
  "decode" should "be able to split a instr correctly" in {

    test(new Decode) { dut =>

      // add x5, x6, x7
      // running test where every reg is 0
      val instr = "h007302B3".U
      dut.io.instrIn.poke(instr)
      dut.io.writeFlag.poke(false.B)

      dut.clock.step(1)

      dut.io.opcodeOut.expect("h33".U)
      dut.io.rs1Out.expect(0.U)
      dut.io.rs2Out.expect(0.U)
      dut.io.funct3Out.expect(0.U)
      dut.io.funct7Out.expect(0.U)

      // now write registers for later usage, register x6 and x7 will both be 5
      dut.io.writeFlag.poke(true.B)
      dut.io.writeAddr.poke(6.U)
      dut.io.writeData.poke(5.U)
      dut.clock.step(1)

      dut.io.writeAddr.poke(7.U)
      dut.clock.step(1)

      // turn off writeflag so no more reg writing
      dut.io.writeFlag.poke(false.B)

      // now check everything again
      dut.io.opcodeOut.expect("h33".U)
      dut.io.rs1Out.expect(5.U)
      dut.io.rs2Out.expect(5.U)
      dut.io.funct3Out.expect(0.U)
      dut.io.funct7Out.expect(0.U)

      // addi x1, x0, 5
      dut.io.instrIn.poke("h00500093".U)
      dut.io.immIOut.expect(5.U)

      // sw x1, 8(x0)
      dut.io.instrIn.poke("h00102423".U)
      dut.io.immSOut.expect(8.U)

      // beq x0, x0, 4
      dut.io.instrIn.poke("h00000263".U)
      dut.io.immBOut.expect(4.U)

      // lui x1, 0x12345
      dut.io.instrIn.poke("h123450B7".U)
      dut.io.immUOut.expect("h12345000".U)

      // jal x0, 8
      dut.io.instrIn.poke("h0080006F".U)
      dut.io.immJOut.expect(8.U)

      // lw x2, 12(x0)
      dut.io.instrIn.poke("h00C02103".U)
      dut.io.immIOut.expect(12.U)
      dut.io.memReadOut.expect(true.B)
      dut.io.memWriteOut.expect(false.B)
      dut.io.wbFlagOut.expect(true.B)
      dut.io.wbALUOrMemOut.expect(true.B)
      dut.io.widthSizeOut.expect("b00".U)
    }
  }
}
