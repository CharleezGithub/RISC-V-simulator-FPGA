import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV extends Module {
  val io = IO(new Bundle {
    val rx = Input(Bool())
    val readyToReadProgram = Input(Bool())
    val runProgram = Input(Bool())
    val tx = Output(Bool())

    val idleLED = Output(Bool())
    val listeningLED = Output(Bool())
    val runningLED = Output(Bool())
  })
  // ---
  // UART and Bootloader section
  // ---
  // Defaults for LEDs
  io.idleLED := false.B
  io.listeningLED := false.B
  io.runningLED := false.B

  val instructionMemory = RegInit(VecInit(Seq.fill(1024)(0.U(32.W))))

  // States
  val idle :: running :: listening :: Nil = Enum(3)
  val state = RegInit(idle)

  // flags
  val uartResponseType = RegInit(0.U(4.W))
  val uartSendResponse = io.runProgram

  val readyPrev = RegInit(false.B)
  val runPrev = RegInit(false.B)

  val readyRise = io.readyToReadProgram && !readyPrev
  val runRise = io.runProgram && !runPrev

  readyPrev := io.readyToReadProgram
  runPrev := io.runProgram

  // Start capturing/listening to uart
  val captureActive = RegInit(false.B)
  when(readyRise) {
    captureActive := true.B
    state := listening
  }
  when(runRise) {
    captureActive := false.B
  }

  when(io.runProgram) {
    uartResponseType := 2.U
    state := running
  }

  // Start UART and connect flags
  val uart = Module(new Uart(100_000_000, 115200))
  uart.io.rx := io.rx
  uart.io.responseType := uartResponseType
  uart.io.sendResponse := uartSendResponse
  uart.io.captureEnable := captureActive
  uart.io.clearBuffer := readyRise

  // Start bootloader
  val bootloaderStart = readyRise

  val bootloader = Module(new UartBootloader())
  bootloader.io.start := bootloaderStart
  bootloader.io.rxValid := uart.io.rxValid
  bootloader.io.rxData := uart.io.rxReadValue

  io.tx := uart.io.tx

  // Load program when bootloader writemem is enabled
  when(bootloader.io.memWriteEn && state === running) {
    instructionMemory(bootloader.io.memWriteAddr) := bootloader.io.memWriteData
  }

  // State machine
  switch(state) {
    is(idle) {
      io.idleLED := true.B
      io.listeningLED := false.B
      io.runningLED := false.B
    }
    is(listening) {
      io.idleLED := false.B
      io.listeningLED := true.B
      io.runningLED := false.B
    }
    is(running) {
      io.idleLED := false.B
      io.listeningLED := false.B
      io.runningLED := true.B
    }
  }

  // ---
  // RISC-V sim section
  // ---
  // Instantiate all stages so we can wire them together
  val fetch = Module(new Fetch)
  val pipeline1 = Module(new IF_ID)
  val decoder = Module(new Decode)
  val pipeline2 = Module(new ID_EX)
  val execute = Module(new Execute)
  val pipeline3 = Module(new EX_MEM)
  val memory = Module(new Memory)
  val pipeline4 = Module(new MEM_WB)
  val writeback = Module(new Writeback)

  // Connecting Fetch - pipeline registers
  fetch.io.program := instructionMemory
  pipeline1.io.instrIn := fetch.io.instr
  pipeline1.io.pcIn := fetch.io.pcOut

  // Connecting pipeline registers - Decoder
  decoder.io.instrIn := pipeline1.io.instr
  decoder.io.pcIn := pipeline1.io.pcOut

  // Connecting Decoder - pipeline registers
  pipeline2.io.rs1In := decoder.io.rs1Out
  pipeline2.io.rs2In := decoder.io.rs2Out
  pipeline2.io.isADDIn := decoder.io.isADDOut
  pipeline2.io.pcIn := decoder.io.pcOut

  // Connecting pipeline registers - Execute
  execute.io.rs1In := pipeline2.io.rs1Out
  execute.io.rs2In := pipeline2.io.rs2Out
  execute.io.isADDIn := pipeline2.io.isADDOut
  execute.io.pcIn := pipeline2.io.pcOut

  // Connecting Execute - pipeline registers
  pipeline3.io.pcIn := execute.io.pcOut
  pipeline3.io.ALUin := execute.io.ALUout

  // Connecting pipeline registers - Memory
  memory.io.ALUin := pipeline3.io.ALUout

  // Connecting Memory - pipeline registers
  pipeline4.io.ALUin := memory.io.ALUout

  // Connecting pipeline registers - Write-back
  writeback.io.ALUin := pipeline4.io.ALUout
}

// generate Verilog
object RISCV extends App {
  emitVerilog(new RISCV())
}
