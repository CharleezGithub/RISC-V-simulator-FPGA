module Tx(
  input   clock,
  input   reset,
  output  io_txd // @[src/main/scala/chisel/lib/uart/Uart.scala 23:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
`endif // RANDOMIZE_REG_INIT
  reg [10:0] shiftReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 30:25]
  reg [19:0] cntReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 31:23]
  reg [3:0] bitsReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 32:24]
  wire  _io_channel_ready_T = cntReg == 20'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 34:31]
  wire [9:0] shift = shiftReg[10:1]; // @[src/main/scala/chisel/lib/uart/Uart.scala 41:28]
  wire [10:0] _shiftReg_T_1 = {1'h1,shift}; // @[src/main/scala/chisel/lib/uart/Uart.scala 42:22]
  wire [3:0] _bitsReg_T_1 = bitsReg - 4'h1; // @[src/main/scala/chisel/lib/uart/Uart.scala 43:26]
  wire [19:0] _cntReg_T_1 = cntReg - 20'h1; // @[src/main/scala/chisel/lib/uart/Uart.scala 54:22]
  assign io_txd = shiftReg[0]; // @[src/main/scala/chisel/lib/uart/Uart.scala 35:21]
  always @(posedge clock) begin
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 30:25]
      shiftReg <= 11'h7ff; // @[src/main/scala/chisel/lib/uart/Uart.scala 30:25]
    end else if (_io_channel_ready_T) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 37:24]
      if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 40:27]
        shiftReg <= _shiftReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 42:16]
      end else begin
        shiftReg <= 11'h7ff;
      end
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 31:23]
      cntReg <= 20'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 31:23]
    end else if (_io_channel_ready_T) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 37:24]
      cntReg <= 20'h363; // @[src/main/scala/chisel/lib/uart/Uart.scala 39:12]
    end else begin
      cntReg <= _cntReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 54:12]
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 32:24]
      bitsReg <= 4'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 32:24]
    end else if (_io_channel_ready_T) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 37:24]
      if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 40:27]
        bitsReg <= _bitsReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 43:15]
      end
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  shiftReg = _RAND_0[10:0];
  _RAND_1 = {1{`RANDOM}};
  cntReg = _RAND_1[19:0];
  _RAND_2 = {1{`RANDOM}};
  bitsReg = _RAND_2[3:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module BufferedTx(
  input   clock,
  input   reset,
  output  io_txd // @[src/main/scala/chisel/lib/uart/Uart.scala 140:14]
);
  wire  tx_clock; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  tx_reset; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  tx_io_txd; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  Tx tx ( // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
    .clock(tx_clock),
    .reset(tx_reset),
    .io_txd(tx_io_txd)
  );
  assign io_txd = tx_io_txd; // @[src/main/scala/chisel/lib/uart/Uart.scala 149:10]
  assign tx_clock = clock;
  assign tx_reset = reset;
endmodule
module UArt(
  input   clock,
  input   reset,
  output  io_tx // @[\\src\\main\\scala\\UArt.scala 6:14]
);
  wire  uart_clock; // @[\\src\\main\\scala\\UArt.scala 13:20]
  wire  uart_reset; // @[\\src\\main\\scala\\UArt.scala 13:20]
  wire  uart_io_txd; // @[\\src\\main\\scala\\UArt.scala 13:20]
  BufferedTx uart ( // @[\\src\\main\\scala\\UArt.scala 13:20]
    .clock(uart_clock),
    .reset(uart_reset),
    .io_txd(uart_io_txd)
  );
  assign io_tx = uart_io_txd; // @[\\src\\main\\scala\\UArt.scala 14:9]
  assign uart_clock = clock;
  assign uart_reset = reset;
endmodule
module RISCV(
  input   clock,
  input   reset,
  output  io_tx // @[\\src\\main\\scala\\main.scala 6:14]
);
  wire  uart_clock; // @[\\src\\main\\scala\\main.scala 10:20]
  wire  uart_reset; // @[\\src\\main\\scala\\main.scala 10:20]
  wire  uart_io_tx; // @[\\src\\main\\scala\\main.scala 10:20]
  UArt uart ( // @[\\src\\main\\scala\\main.scala 10:20]
    .clock(uart_clock),
    .reset(uart_reset),
    .io_tx(uart_io_tx)
  );
  assign io_tx = uart_io_tx; // @[\\src\\main\\scala\\main.scala 16:9]
  assign uart_clock = clock;
  assign uart_reset = reset;
endmodule
