import chisel3._
import chisel3.util._
import chisel.lib.uart._

class RISCV extends Module {
    val io = IO(new Bundle {
        val rx = Input(Bool())
        val readyToReadProgram = Input(Bool())
        val runProgram = Input(Bool())
        val printRegs = Input(Bool())

        val tx = Output(Bool())

        val idleLED = Output(Bool())
        val listeningLED = Output(Bool())
        val runningLED = Output(Bool())
        val printRegLED = Output(Bool())
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

    val readyPrev = RegInit(false.B)
    val runPrev = RegInit(false.B)

    val readyRise = io.readyToReadProgram && !readyPrev
    val runRise = io.runProgram && !runPrev

    val uartResponseType = Wire(UInt(4.W))

    val printPrev = RegInit(false.B)
    val printRise = io.printRegs && !printPrev
    printPrev := io.printRegs

    io.printRegLED := io.printRegs

    val uartSendResponse = runRise || printRise

    readyPrev := io.readyToReadProgram
    runPrev := io.runProgram

    // Start capturing/listening to uart
    val captureActive = RegInit(false.B)
    when(readyRise) {
        captureActive := true.B
        state := listening
    }

    uartResponseType := 0.U

    when(runRise) {
        uartResponseType := 2.U
        captureActive := false.B
    }
    when(printRise) {
        uartResponseType := 3.U
        captureActive := false.B
    }

    when(io.runProgram) {
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
    when(bootloader.io.memWriteEn && state === listening) {
        instructionMemory(
            bootloader.io.memWriteAddr
        ) := bootloader.io.memWriteData
    }

    // Latching ALUL Out for debug print
    val latchingALU = RegInit(0.U(32.W))

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

    // --
    // State machine
    // --
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

    // Connecting Fetch - pipeline registers
    fetch.io.program := instructionMemory
    pipeline1.io.instrIn := fetch.io.instr
    pipeline1.io.pcIn := fetch.io.pcOut

    // Connecting pipeline registers - Decoder
    decoder.io.instrIn := pipeline1.io.instr
    decoder.io.pcIn := pipeline1.io.pcOut

    decoder.io.writeFlag := false.B
    decoder.io.writeAddr := 0.U(5.W)
    decoder.io.writeData := 0.U(32.W)

    // Connecting Decoder - pipeline registers
    pipeline2.io.rdaddrIn := decoder.io.rdaddrOut
    pipeline2.io.rs1In := decoder.io.rs1Out
    pipeline2.io.rs2In := decoder.io.rs2Out
    pipeline2.io.pcIn := decoder.io.pcOut
        //control signals
    pipeline2.io.widthSizeIn := decoder.io.widthSizeOut
    pipeline2.io.memWriteIn := decoder.io.memWriteOut
    pipeline2.io.memReadIn := decoder.io.memReadOut
    pipeline2.io.wbFlagIn := decoder.io.wbFlagOut
    pipeline2.io.wbALUOrMemIn := decoder.io.wbALUOrMemOut

    pipeline2.io.opcodeIn := decoder.io.opcodeOut
    pipeline2.io.funct3In := decoder.io.funct3Out
    pipeline2.io.funct7In := decoder.io.funct7Out

    pipeline2.io.immIIn := decoder.io.immIOut
    pipeline2.io.immSIn := decoder.io.immSOut
    pipeline2.io.immBIn := decoder.io.immBOut
    pipeline2.io.immUIn := decoder.io.immUOut
    pipeline2.io.immJIn := decoder.io.immJOut

    // Connecting pipeline registers - Execute

        //control signals
    pipeline3.io.widthSizeIn := pipeline2.io.widthSizeOut
    pipeline3.io.memWriteIn := pipeline2.io.memWriteOut
    pipeline3.io.memReadIn := pipeline2.io.memReadIn
    pipeline3.io.wbFlagIn := pipeline2.io.wbFlagOut
    pipeline3.io.wbALUOrMemOut := pipeline2.io.wbALUOrMemOut


    execute.io.rdaddr := pipeline2.io.rdaddrOut
    execute.io.rs1Data := pipeline2.io.rs1Out
    execute.io.rs2Data := pipeline2.io.rs2Out
    execute.io.pcIn := pipeline2.io.pcOut

    execute.io.opcode := pipeline2.io.opcodeOut
    execute.io.funct3 := pipeline2.io.funct3Out
    execute.io.funct7 := pipeline2.io.funct7Out

    execute.io.immI := pipeline2.io.immIOut
    execute.io.immS := pipeline2.io.immSOut
    execute.io.immB := pipeline2.io.immBOut
    execute.io.immU := pipeline2.io.immUOut
    execute.io.immJ := pipeline2.io.immJOut

    // Connecting Execute - pipeline registers
    pipeline3.io.rdaddrIn := execute.io.rdaddrOut
    pipeline3.io.pcIn := execute.io.pcOut
    pipeline3.io.ALUIn := execute.io.ALUOut

    // Connecting pipeline registers - Memory
    memory.io.ALUIn := pipeline3.io.ALUOut
    memory.io.rdaddrIn := pipeline3.io.rdaddrOut
    memory.io.widthSizeIn := pipeline3.io.widthSizeOut
    memory.io.memWriteIn := pipeline3.io.memWriteOut
    memory.io.memReadIn := pipeline3.io.memReadOut
    memory.io.wbFlagIn := pipeline3.io.wbFlagOut
    memory.io.wbALUOrMemIn := pipeline3.io.wbALUOrMemOut
    
        //control signals
    pipeline4.io.widthSizeIn := pipeline3.io.widthSizeOut
    pipeline4.io.memWriteIn := pipeline3.io.memWriteOut
    pipeline4.io.memReadIn := pipeline3.io.memReadOut
    pipeline4.io.wbFlagIn := pipeline3.io.wbFlagOut
    pipeline4.io.wbALUOrMemIn := pipeline3.io.wbALUOrMemOut

    // Connecting Memory - pipeline registers
    pipeline4.io.ALUIn := memory.io.ALUOut
    pipeline4.io.rdaddrIn := memory.io.rdaddrOut

    // Connecting pipeline registers - Write-back
    writeback.io.widthSizeIn := pipeline4.io.widthSizeOut
    writeback.io.memWriteIn := pipeline4.io.memWriteOut
    writeback.io.memReadIn := pipeline4.io.memReadOut
    writeback.io.wbFlagIn := pipeline4.io.wbFlagOut
    writeback.io.wbALUOrMemIn := pipeline4.io.wbALUOrMemOut

    writeback.io.ALUIn := pipeline4.io.ALUOut
    writeback.io.rdaddr := pineline4.io.rdaddrOut


    latchingALU := Mux(
        execute.io.ALUOut === 0.U,
        latchingALU,
        execute.io.ALUOut
    )

    uart.io.printOutRegs(0) := latchingALU
}

// generate Verilog
object RISCV extends App {
    emitVerilog(new RISCV())
}
