import chisel3._
import chisel3.util._
import chisel.lib.uart._

/** UART echo: receives bytes on rx and transmits the exact same bytes on tx. */
class UArt(frequ: Int, baud: Int = 115200) extends Module {
  val io = IO(new Bundle {
    val rx = Input(Bool())
    val tx = Output(Bool())
  })

  val uartTx = Module(new BufferedTx(frequ, baud))
  val uartRx = Module(new BufferedRx(frequ, baud))

  io.tx := uartTx.io.txd
  uartRx.io.rxd := io.rx

  // Small FIFO to decouple RX from TX (helps if TX is briefly busy)
  val fifo = Module(new Queue(UInt(8.W), entries = 16))

  // RX -> FIFO
  fifo.io.enq.valid := uartRx.io.channel.valid
  fifo.io.enq.bits := uartRx.io.channel.bits
  uartRx.io.channel.ready := fifo.io.enq.ready

  // FIFO -> TX
  uartTx.io.channel.valid := fifo.io.deq.valid
  uartTx.io.channel.bits := fifo.io.deq.bits
  fifo.io.deq.ready := uartTx.io.channel.ready
}

// Verilog generation
object UArtMain extends App {
  emitVerilog(new UArt(100_000_000), Array("--target-dir", "generated"))
}
