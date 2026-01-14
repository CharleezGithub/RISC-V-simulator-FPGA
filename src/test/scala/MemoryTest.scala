package empty

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class MemoryTest extends AnyFlatSpec with ChiselScalatestTester {

    behavior of "Memory stage"

    it should "store and load a full word correctly" in {
        test(new Memory) { dut =>
            // Store word 0xDEADBEEF at address 0
            dut.io.ALUIn.poke(0.U) // address
            dut.io.rs2DataIn.poke("hDEADBEEF".U)
            dut.io.widthSizeIn.poke("b10".U) // word
            dut.io.memWriteIn.poke(true.B)
            dut.io.memReadIn.poke(false.B)

            dut.clock.step()

            // Disable write
            dut.io.memWriteIn.poke(false.B)

            // Load word
            dut.io.memReadIn.poke(true.B)
            dut.clock.step()

            dut.io.loadDataOut.expect("hDEADBEEF".U)
        }
    }

    it should "store and load a byte with sign extension" in {
        test(new Memory) { dut =>
            // Store byte 0x80 (negative if signed)
            dut.io.ALUIn.poke(0.U)
            dut.io.rs2DataIn.poke("h00000080".U)
            dut.io.widthSizeIn.poke("b00".U) // byte
            dut.io.memWriteIn.poke(true.B)
            dut.io.memReadIn.poke(false.B)

            dut.clock.step()

            // Load byte
            dut.io.memWriteIn.poke(false.B)
            dut.io.memReadIn.poke(true.B)
            dut.clock.step()

            // Sign-extended byte: 0xFFFFFF80
            dut.io.loadDataOut.expect("hFFFFFF80".U)
        }
    }

    it should "store and load a halfword with sign extension" in {
        test(new Memory) { dut =>
            // Store halfword 0x8001
            dut.io.ALUIn.poke(0.U)
            dut.io.rs2DataIn.poke("h00008001".U)
            dut.io.widthSizeIn.poke("b01".U) // halfword
            dut.io.memWriteIn.poke(true.B)
            dut.io.memReadIn.poke(false.B)

            dut.clock.step()

            // Load halfword
            dut.io.memWriteIn.poke(false.B)
            dut.io.memReadIn.poke(true.B)
            dut.clock.step()

            // Sign-extended halfword: 0xFFFF8001
            dut.io.loadDataOut.expect("hFFFF8001".U)
        }
    }

    it should "pass through ALUOut and rdaddrOut unchanged" in {
        test(new Memory) { dut =>
            dut.io.ALUIn.poke("h12345678".U)
            dut.io.rdaddrIn.poke(7.U)

            dut.clock.step()

            dut.io.ALUOut.expect("h12345678".U)
            dut.io.rdaddrOut.expect(7.U)
        }
    }
}
