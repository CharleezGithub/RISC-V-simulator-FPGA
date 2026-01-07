module Rx(
  input        clock,
  input        reset,
  input        io_rxd, // @[src/main/scala/chisel/lib/uart/Uart.scala 67:14]
  input        io_channel_ready, // @[src/main/scala/chisel/lib/uart/Uart.scala 67:14]
  output       io_channel_valid, // @[src/main/scala/chisel/lib/uart/Uart.scala 67:14]
  output [7:0] io_channel_bits // @[src/main/scala/chisel/lib/uart/Uart.scala 67:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
`endif // RANDOMIZE_REG_INIT
  reg  rxReg_REG; // @[src/main/scala/chisel/lib/uart/Uart.scala 76:30]
  reg  rxReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 76:22]
  reg  falling_REG; // @[src/main/scala/chisel/lib/uart/Uart.scala 77:35]
  wire  falling = ~rxReg & falling_REG; // @[src/main/scala/chisel/lib/uart/Uart.scala 77:24]
  reg [7:0] shiftReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 79:25]
  reg [19:0] cntReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 80:23]
  reg [3:0] bitsReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 81:24]
  reg  valReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 82:23]
  wire [19:0] _cntReg_T_1 = cntReg - 20'h1; // @[src/main/scala/chisel/lib/uart/Uart.scala 85:22]
  wire [7:0] _shiftReg_T_1 = {rxReg,shiftReg[7:1]}; // @[src/main/scala/chisel/lib/uart/Uart.scala 88:20]
  wire [3:0] _bitsReg_T_1 = bitsReg - 4'h1; // @[src/main/scala/chisel/lib/uart/Uart.scala 89:24]
  wire  _GEN_0 = bitsReg == 4'h1 | valReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 91:27 92:14 82:23]
  assign io_channel_valid = valReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 104:20]
  assign io_channel_bits = shiftReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 103:19]
  always @(posedge clock) begin
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 76:30]
      rxReg_REG <= 1'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 76:30]
    end else begin
      rxReg_REG <= io_rxd; // @[src/main/scala/chisel/lib/uart/Uart.scala 76:30]
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 76:22]
      rxReg <= 1'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 76:22]
    end else begin
      rxReg <= rxReg_REG; // @[src/main/scala/chisel/lib/uart/Uart.scala 76:22]
    end
    falling_REG <= rxReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 77:35]
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 79:25]
      shiftReg <= 8'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 79:25]
    end else if (!(cntReg != 20'h0)) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 84:24]
      if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 86:31]
        shiftReg <= _shiftReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 88:14]
      end
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 80:23]
      cntReg <= 20'h363; // @[src/main/scala/chisel/lib/uart/Uart.scala 80:23]
    end else if (cntReg != 20'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 84:24]
      cntReg <= _cntReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 85:12]
    end else if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 86:31]
      cntReg <= 20'h363; // @[src/main/scala/chisel/lib/uart/Uart.scala 87:12]
    end else if (falling) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 94:23]
      cntReg <= 20'h514; // @[src/main/scala/chisel/lib/uart/Uart.scala 95:12]
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 81:24]
      bitsReg <= 4'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 81:24]
    end else if (!(cntReg != 20'h0)) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 84:24]
      if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 86:31]
        bitsReg <= _bitsReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 89:13]
      end else if (falling) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 94:23]
        bitsReg <= 4'h8; // @[src/main/scala/chisel/lib/uart/Uart.scala 96:13]
      end
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 82:23]
      valReg <= 1'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 82:23]
    end else if (valReg & io_channel_ready) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 99:36]
      valReg <= 1'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 100:12]
    end else if (!(cntReg != 20'h0)) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 84:24]
      if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 86:31]
        valReg <= _GEN_0;
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
  rxReg_REG = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  rxReg = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  falling_REG = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  shiftReg = _RAND_3[7:0];
  _RAND_4 = {1{`RANDOM}};
  cntReg = _RAND_4[19:0];
  _RAND_5 = {1{`RANDOM}};
  bitsReg = _RAND_5[3:0];
  _RAND_6 = {1{`RANDOM}};
  valReg = _RAND_6[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module Tx(
  input        clock,
  input        reset,
  output       io_txd, // @[src/main/scala/chisel/lib/uart/Uart.scala 23:14]
  output       io_channel_ready, // @[src/main/scala/chisel/lib/uart/Uart.scala 23:14]
  input        io_channel_valid, // @[src/main/scala/chisel/lib/uart/Uart.scala 23:14]
  input  [7:0] io_channel_bits // @[src/main/scala/chisel/lib/uart/Uart.scala 23:14]
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
  wire [10:0] _shiftReg_T_3 = {2'h3,io_channel_bits,1'h0}; // @[src/main/scala/chisel/lib/uart/Uart.scala 46:24]
  wire [19:0] _cntReg_T_1 = cntReg - 20'h1; // @[src/main/scala/chisel/lib/uart/Uart.scala 54:22]
  assign io_txd = shiftReg[0]; // @[src/main/scala/chisel/lib/uart/Uart.scala 35:21]
  assign io_channel_ready = cntReg == 20'h0 & bitsReg == 4'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 34:40]
  always @(posedge clock) begin
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 30:25]
      shiftReg <= 11'h7ff; // @[src/main/scala/chisel/lib/uart/Uart.scala 30:25]
    end else if (_io_channel_ready_T) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 37:24]
      if (bitsReg != 4'h0) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 40:27]
        shiftReg <= _shiftReg_T_1; // @[src/main/scala/chisel/lib/uart/Uart.scala 42:16]
      end else if (io_channel_valid) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 45:30]
        shiftReg <= _shiftReg_T_3; // @[src/main/scala/chisel/lib/uart/Uart.scala 46:18]
      end else begin
        shiftReg <= 11'h7ff; // @[src/main/scala/chisel/lib/uart/Uart.scala 49:18]
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
      end else if (io_channel_valid) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 45:30]
        bitsReg <= 4'hb; // @[src/main/scala/chisel/lib/uart/Uart.scala 47:17]
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
module Buffer(
  input        clock,
  input        reset,
  output       io_in_ready, // @[src/main/scala/chisel/lib/uart/Uart.scala 111:14]
  input        io_in_valid, // @[src/main/scala/chisel/lib/uart/Uart.scala 111:14]
  input  [7:0] io_in_bits, // @[src/main/scala/chisel/lib/uart/Uart.scala 111:14]
  input        io_out_ready, // @[src/main/scala/chisel/lib/uart/Uart.scala 111:14]
  output       io_out_valid, // @[src/main/scala/chisel/lib/uart/Uart.scala 111:14]
  output [7:0] io_out_bits // @[src/main/scala/chisel/lib/uart/Uart.scala 111:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
`endif // RANDOMIZE_REG_INIT
  reg  stateReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 117:25]
  reg [7:0] dataReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 118:24]
  wire  _io_in_ready_T = ~stateReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 120:27]
  wire  _GEN_1 = io_in_valid | stateReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 124:23 126:16 117:25]
  assign io_in_ready = ~stateReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 120:27]
  assign io_out_valid = stateReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 121:28]
  assign io_out_bits = dataReg; // @[src/main/scala/chisel/lib/uart/Uart.scala 133:15]
  always @(posedge clock) begin
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 117:25]
      stateReg <= 1'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 117:25]
    end else if (_io_in_ready_T) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 123:28]
      stateReg <= _GEN_1;
    end else if (io_out_ready) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 129:24]
      stateReg <= 1'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 130:16]
    end
    if (reset) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 118:24]
      dataReg <= 8'h0; // @[src/main/scala/chisel/lib/uart/Uart.scala 118:24]
    end else if (_io_in_ready_T) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 123:28]
      if (io_in_valid) begin // @[src/main/scala/chisel/lib/uart/Uart.scala 124:23]
        dataReg <= io_in_bits; // @[src/main/scala/chisel/lib/uart/Uart.scala 125:15]
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
  stateReg = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  dataReg = _RAND_1[7:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module BufferedTx(
  input        clock,
  input        reset,
  output       io_txd, // @[src/main/scala/chisel/lib/uart/Uart.scala 140:14]
  output       io_channel_ready, // @[src/main/scala/chisel/lib/uart/Uart.scala 140:14]
  input        io_channel_valid, // @[src/main/scala/chisel/lib/uart/Uart.scala 140:14]
  input  [7:0] io_channel_bits // @[src/main/scala/chisel/lib/uart/Uart.scala 140:14]
);
  wire  tx_clock; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  tx_reset; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  tx_io_txd; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  tx_io_channel_ready; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  tx_io_channel_valid; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire [7:0] tx_io_channel_bits; // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
  wire  buf__clock; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire  buf__reset; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire  buf__io_in_ready; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire  buf__io_in_valid; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire [7:0] buf__io_in_bits; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire  buf__io_out_ready; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire  buf__io_out_valid; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  wire [7:0] buf__io_out_bits; // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
  Tx tx ( // @[src/main/scala/chisel/lib/uart/Uart.scala 144:18]
    .clock(tx_clock),
    .reset(tx_reset),
    .io_txd(tx_io_txd),
    .io_channel_ready(tx_io_channel_ready),
    .io_channel_valid(tx_io_channel_valid),
    .io_channel_bits(tx_io_channel_bits)
  );
  Buffer buf_ ( // @[src/main/scala/chisel/lib/uart/Uart.scala 145:19]
    .clock(buf__clock),
    .reset(buf__reset),
    .io_in_ready(buf__io_in_ready),
    .io_in_valid(buf__io_in_valid),
    .io_in_bits(buf__io_in_bits),
    .io_out_ready(buf__io_out_ready),
    .io_out_valid(buf__io_out_valid),
    .io_out_bits(buf__io_out_bits)
  );
  assign io_txd = tx_io_txd; // @[src/main/scala/chisel/lib/uart/Uart.scala 149:10]
  assign io_channel_ready = buf__io_in_ready; // @[src/main/scala/chisel/lib/uart/Uart.scala 147:13]
  assign tx_clock = clock;
  assign tx_reset = reset;
  assign tx_io_channel_valid = buf__io_out_valid; // @[src/main/scala/chisel/lib/uart/Uart.scala 148:17]
  assign tx_io_channel_bits = buf__io_out_bits; // @[src/main/scala/chisel/lib/uart/Uart.scala 148:17]
  assign buf__clock = clock;
  assign buf__reset = reset;
  assign buf__io_in_valid = io_channel_valid; // @[src/main/scala/chisel/lib/uart/Uart.scala 147:13]
  assign buf__io_in_bits = io_channel_bits; // @[src/main/scala/chisel/lib/uart/Uart.scala 147:13]
  assign buf__io_out_ready = tx_io_channel_ready; // @[src/main/scala/chisel/lib/uart/Uart.scala 148:17]
endmodule
module Queue(
  input   clock,
  input   reset,
  output  io_enq_ready, // @[src/main/scala/chisel3/util/Decoupled.scala 278:14]
  input   io_enq_valid // @[src/main/scala/chisel3/util/Decoupled.scala 278:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
`endif // RANDOMIZE_REG_INIT
  reg [4:0] enq_ptr_value; // @[src/main/scala/chisel3/util/Counter.scala 61:40]
  reg  maybe_full; // @[src/main/scala/chisel3/util/Decoupled.scala 282:27]
  wire  ptr_match = enq_ptr_value == 5'h0; // @[src/main/scala/chisel3/util/Decoupled.scala 283:33]
  wire  full = ptr_match & maybe_full; // @[src/main/scala/chisel3/util/Decoupled.scala 285:24]
  wire  do_enq = io_enq_ready & io_enq_valid; // @[src/main/scala/chisel3/util/Decoupled.scala 57:35]
  wire [4:0] _value_T_1 = enq_ptr_value + 5'h1; // @[src/main/scala/chisel3/util/Counter.scala 77:24]
  assign io_enq_ready = ~full; // @[src/main/scala/chisel3/util/Decoupled.scala 309:19]
  always @(posedge clock) begin
    if (reset) begin // @[src/main/scala/chisel3/util/Counter.scala 61:40]
      enq_ptr_value <= 5'h0; // @[src/main/scala/chisel3/util/Counter.scala 61:40]
    end else if (do_enq) begin // @[src/main/scala/chisel3/util/Decoupled.scala 292:16]
      enq_ptr_value <= _value_T_1; // @[src/main/scala/chisel3/util/Counter.scala 77:15]
    end
    if (reset) begin // @[src/main/scala/chisel3/util/Decoupled.scala 282:27]
      maybe_full <= 1'h0; // @[src/main/scala/chisel3/util/Decoupled.scala 282:27]
    end else if (do_enq) begin // @[src/main/scala/chisel3/util/Decoupled.scala 299:27]
      maybe_full <= do_enq; // @[src/main/scala/chisel3/util/Decoupled.scala 300:16]
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
  enq_ptr_value = _RAND_0[4:0];
  _RAND_1 = {1{`RANDOM}};
  maybe_full = _RAND_1[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module Uart(
  input        clock,
  input        reset,
  input        io_rx, // @[\\src\\main\\scala\\Uart.scala 7:14]
  input        io_sendResponse, // @[\\src\\main\\scala\\Uart.scala 7:14]
  input  [3:0] io_responseType, // @[\\src\\main\\scala\\Uart.scala 7:14]
  input        io_captureEnable, // @[\\src\\main\\scala\\Uart.scala 7:14]
  input        io_clearBuffer, // @[\\src\\main\\scala\\Uart.scala 7:14]
  output       io_tx // @[\\src\\main\\scala\\Uart.scala 7:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
  reg [31:0] _RAND_8;
  reg [31:0] _RAND_9;
  reg [31:0] _RAND_10;
  reg [31:0] _RAND_11;
  reg [31:0] _RAND_12;
  reg [31:0] _RAND_13;
  reg [31:0] _RAND_14;
  reg [31:0] _RAND_15;
  reg [31:0] _RAND_16;
  reg [31:0] _RAND_17;
  reg [31:0] _RAND_18;
  reg [31:0] _RAND_19;
  reg [31:0] _RAND_20;
  reg [31:0] _RAND_21;
  reg [31:0] _RAND_22;
  reg [31:0] _RAND_23;
  reg [31:0] _RAND_24;
  reg [31:0] _RAND_25;
  reg [31:0] _RAND_26;
  reg [31:0] _RAND_27;
  reg [31:0] _RAND_28;
  reg [31:0] _RAND_29;
  reg [31:0] _RAND_30;
  reg [31:0] _RAND_31;
  reg [31:0] _RAND_32;
  reg [31:0] _RAND_33;
  reg [31:0] _RAND_34;
  reg [31:0] _RAND_35;
`endif // RANDOMIZE_REG_INIT
  wire  rxm_clock; // @[\\src\\main\\scala\\Uart.scala 22:19]
  wire  rxm_reset; // @[\\src\\main\\scala\\Uart.scala 22:19]
  wire  rxm_io_rxd; // @[\\src\\main\\scala\\Uart.scala 22:19]
  wire  rxm_io_channel_ready; // @[\\src\\main\\scala\\Uart.scala 22:19]
  wire  rxm_io_channel_valid; // @[\\src\\main\\scala\\Uart.scala 22:19]
  wire [7:0] rxm_io_channel_bits; // @[\\src\\main\\scala\\Uart.scala 22:19]
  wire  txm_clock; // @[\\src\\main\\scala\\Uart.scala 23:19]
  wire  txm_reset; // @[\\src\\main\\scala\\Uart.scala 23:19]
  wire  txm_io_txd; // @[\\src\\main\\scala\\Uart.scala 23:19]
  wire  txm_io_channel_ready; // @[\\src\\main\\scala\\Uart.scala 23:19]
  wire  txm_io_channel_valid; // @[\\src\\main\\scala\\Uart.scala 23:19]
  wire [7:0] txm_io_channel_bits; // @[\\src\\main\\scala\\Uart.scala 23:19]
  wire  inQ_clock; // @[\\src\\main\\scala\\Uart.scala 49:19]
  wire  inQ_reset; // @[\\src\\main\\scala\\Uart.scala 49:19]
  wire  inQ_io_enq_ready; // @[\\src\\main\\scala\\Uart.scala 49:19]
  wire  inQ_io_enq_valid; // @[\\src\\main\\scala\\Uart.scala 49:19]
  reg [7:0] rxStored_0; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_1; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_2; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_3; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_4; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_5; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_6; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_7; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_8; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_9; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_10; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_11; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_12; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_13; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_14; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_15; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_16; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_17; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_18; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_19; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_20; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_21; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_22; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_23; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_24; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_25; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_26; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_27; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_28; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_29; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_30; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [7:0] rxStored_31; // @[\\src\\main\\scala\\Uart.scala 25:25]
  reg [5:0] rxCount; // @[\\src\\main\\scala\\Uart.scala 26:24]
  wire  rxFire = rxm_io_channel_valid & rxm_io_channel_ready; // @[\\src\\main\\scala\\Uart.scala 36:37]
  wire [7:0] _rxStored_T_3 = rxm_io_channel_bits; // @[\\src\\main\\scala\\Uart.scala 44:{23,23}]
  wire [5:0] _rxCount_T_1 = rxCount + 6'h1; // @[\\src\\main\\scala\\Uart.scala 45:24]
  wire [3:0] _msgSel_T_1 = io_responseType <= 4'h2 ? io_responseType : 4'h3; // @[\\src\\main\\scala\\Uart.scala 71:16]
  reg [5:0] idx; // @[\\src\\main\\scala\\Uart.scala 75:20]
  reg  sending; // @[\\src\\main\\scala\\Uart.scala 76:24]
  reg  sendPrev; // @[\\src\\main\\scala\\Uart.scala 79:25]
  wire  sendRise = io_sendResponse & ~sendPrev; // @[\\src\\main\\scala\\Uart.scala 80:34]
  wire  _GEN_66 = sendRise & ~sending | sending; // @[\\src\\main\\scala\\Uart.scala 87:30 88:13 76:24]
  wire [5:0] _GEN_67 = sendRise & ~sending ? 6'h0 : idx; // @[\\src\\main\\scala\\Uart.scala 75:20 87:30 89:9]
  wire [1:0] msgSel = _msgSel_T_1[1:0]; // @[\\src\\main\\scala\\Uart.scala 70:20 71:10]
  wire [7:0] _GEN_69 = 2'h0 == msgSel & 6'h1 == idx ? 8'h72 : 8'h50; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_70 = 2'h0 == msgSel & 6'h2 == idx ? 8'h6f : _GEN_69; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_71 = 2'h0 == msgSel & 6'h3 == idx ? 8'h67 : _GEN_70; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_72 = 2'h0 == msgSel & 6'h4 == idx ? 8'h72 : _GEN_71; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_73 = 2'h0 == msgSel & 6'h5 == idx ? 8'h61 : _GEN_72; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_74 = 2'h0 == msgSel & 6'h6 == idx ? 8'h6d : _GEN_73; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_75 = 2'h0 == msgSel & 6'h7 == idx ? 8'h20 : _GEN_74; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_76 = 2'h0 == msgSel & 6'h8 == idx ? 8'h52 : _GEN_75; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_77 = 2'h0 == msgSel & 6'h9 == idx ? 8'h65 : _GEN_76; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_78 = 2'h0 == msgSel & 6'ha == idx ? 8'h63 : _GEN_77; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_79 = 2'h0 == msgSel & 6'hb == idx ? 8'h65 : _GEN_78; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_80 = 2'h0 == msgSel & 6'hc == idx ? 8'h69 : _GEN_79; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_81 = 2'h0 == msgSel & 6'hd == idx ? 8'h76 : _GEN_80; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_82 = 2'h0 == msgSel & 6'he == idx ? 8'h65 : _GEN_81; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_83 = 2'h0 == msgSel & 6'hf == idx ? 8'h64 : _GEN_82; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_84 = 2'h0 == msgSel & 6'h10 == idx ? 8'h21 : _GEN_83; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_85 = 2'h0 == msgSel & 6'h11 == idx ? 8'hd : _GEN_84; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_86 = 2'h0 == msgSel & 6'h12 == idx ? 8'ha : _GEN_85; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_87 = 2'h0 == msgSel & 6'h13 == idx ? 8'h20 : _GEN_86; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_88 = 2'h0 == msgSel & 6'h14 == idx ? 8'h20 : _GEN_87; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_89 = 2'h0 == msgSel & 6'h15 == idx ? 8'h20 : _GEN_88; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_90 = 2'h0 == msgSel & 6'h16 == idx ? 8'h20 : _GEN_89; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_91 = 2'h0 == msgSel & 6'h17 == idx ? 8'h20 : _GEN_90; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_92 = 2'h0 == msgSel & 6'h18 == idx ? 8'h20 : _GEN_91; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_93 = 2'h0 == msgSel & 6'h19 == idx ? 8'h20 : _GEN_92; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_94 = 2'h0 == msgSel & 6'h1a == idx ? 8'h20 : _GEN_93; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_95 = 2'h0 == msgSel & 6'h1b == idx ? 8'h20 : _GEN_94; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_96 = 2'h0 == msgSel & 6'h1c == idx ? 8'h20 : _GEN_95; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_97 = 2'h0 == msgSel & 6'h1d == idx ? 8'h20 : _GEN_96; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_98 = 2'h0 == msgSel & 6'h1e == idx ? 8'h20 : _GEN_97; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_99 = 2'h0 == msgSel & 6'h1f == idx ? 8'h20 : _GEN_98; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_100 = 2'h0 == msgSel & 6'h20 == idx ? 8'h20 : _GEN_99; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_101 = 2'h0 == msgSel & 6'h21 == idx ? 8'h20 : _GEN_100; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_102 = 2'h0 == msgSel & 6'h22 == idx ? 8'h20 : _GEN_101; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_103 = 2'h0 == msgSel & 6'h23 == idx ? 8'h20 : _GEN_102; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_104 = 2'h0 == msgSel & 6'h24 == idx ? 8'h20 : _GEN_103; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_105 = 2'h0 == msgSel & 6'h25 == idx ? 8'h20 : _GEN_104; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_106 = 2'h0 == msgSel & 6'h26 == idx ? 8'h20 : _GEN_105; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_107 = 2'h0 == msgSel & 6'h27 == idx ? 8'h20 : _GEN_106; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_108 = 2'h0 == msgSel & 6'h28 == idx ? 8'h20 : _GEN_107; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_109 = 2'h0 == msgSel & 6'h29 == idx ? 8'h20 : _GEN_108; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_110 = 2'h0 == msgSel & 6'h2a == idx ? 8'h20 : _GEN_109; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_111 = 2'h0 == msgSel & 6'h2b == idx ? 8'h20 : _GEN_110; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_112 = 2'h0 == msgSel & 6'h2c == idx ? 8'h20 : _GEN_111; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_113 = 2'h0 == msgSel & 6'h2d == idx ? 8'h20 : _GEN_112; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_114 = 2'h0 == msgSel & 6'h2e == idx ? 8'h20 : _GEN_113; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_115 = 2'h0 == msgSel & 6'h2f == idx ? 8'h20 : _GEN_114; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_116 = 2'h0 == msgSel & 6'h30 == idx ? 8'h20 : _GEN_115; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_117 = 2'h0 == msgSel & 6'h31 == idx ? 8'h20 : _GEN_116; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_118 = 2'h0 == msgSel & 6'h32 == idx ? 8'h20 : _GEN_117; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_119 = 2'h0 == msgSel & 6'h33 == idx ? 8'h20 : _GEN_118; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_120 = 2'h0 == msgSel & 6'h34 == idx ? 8'h20 : _GEN_119; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_121 = 2'h0 == msgSel & 6'h35 == idx ? 8'h20 : _GEN_120; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_122 = 2'h0 == msgSel & 6'h36 == idx ? 8'h20 : _GEN_121; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_123 = 2'h0 == msgSel & 6'h37 == idx ? 8'h20 : _GEN_122; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_124 = 2'h0 == msgSel & 6'h38 == idx ? 8'h20 : _GEN_123; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_125 = 2'h0 == msgSel & 6'h39 == idx ? 8'h20 : _GEN_124; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_126 = 2'h0 == msgSel & 6'h3a == idx ? 8'h20 : _GEN_125; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_127 = 2'h0 == msgSel & 6'h3b == idx ? 8'h20 : _GEN_126; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_128 = 2'h0 == msgSel & 6'h3c == idx ? 8'h20 : _GEN_127; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_129 = 2'h0 == msgSel & 6'h3d == idx ? 8'h20 : _GEN_128; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_130 = 2'h0 == msgSel & 6'h3e == idx ? 8'h20 : _GEN_129; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_131 = 2'h0 == msgSel & 6'h3f == idx ? 8'h20 : _GEN_130; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_132 = 2'h1 == msgSel & 6'h0 == idx ? 8'h44 : _GEN_131; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_133 = 2'h1 == msgSel & 6'h1 == idx ? 8'h75 : _GEN_132; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_134 = 2'h1 == msgSel & 6'h2 == idx ? 8'h6d : _GEN_133; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_135 = 2'h1 == msgSel & 6'h3 == idx ? 8'h6d : _GEN_134; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_136 = 2'h1 == msgSel & 6'h4 == idx ? 8'h79 : _GEN_135; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_137 = 2'h1 == msgSel & 6'h5 == idx ? 8'h20 : _GEN_136; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_138 = 2'h1 == msgSel & 6'h6 == idx ? 8'h50 : _GEN_137; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_139 = 2'h1 == msgSel & 6'h7 == idx ? 8'h72 : _GEN_138; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_140 = 2'h1 == msgSel & 6'h8 == idx ? 8'h6f : _GEN_139; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_141 = 2'h1 == msgSel & 6'h9 == idx ? 8'h66 : _GEN_140; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_142 = 2'h1 == msgSel & 6'ha == idx ? 8'h69 : _GEN_141; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_143 = 2'h1 == msgSel & 6'hb == idx ? 8'h6c : _GEN_142; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_144 = 2'h1 == msgSel & 6'hc == idx ? 8'h69 : _GEN_143; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_145 = 2'h1 == msgSel & 6'hd == idx ? 8'h6e : _GEN_144; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_146 = 2'h1 == msgSel & 6'he == idx ? 8'h67 : _GEN_145; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_147 = 2'h1 == msgSel & 6'hf == idx ? 8'h20 : _GEN_146; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_148 = 2'h1 == msgSel & 6'h10 == idx ? 8'h44 : _GEN_147; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_149 = 2'h1 == msgSel & 6'h11 == idx ? 8'h61 : _GEN_148; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_150 = 2'h1 == msgSel & 6'h12 == idx ? 8'h74 : _GEN_149; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_151 = 2'h1 == msgSel & 6'h13 == idx ? 8'h61 : _GEN_150; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_152 = 2'h1 == msgSel & 6'h14 == idx ? 8'h21 : _GEN_151; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_153 = 2'h1 == msgSel & 6'h15 == idx ? 8'h20 : _GEN_152; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_154 = 2'h1 == msgSel & 6'h16 == idx ? 8'h28 : _GEN_153; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_155 = 2'h1 == msgSel & 6'h17 == idx ? 8'h4e : _GEN_154; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_156 = 2'h1 == msgSel & 6'h18 == idx ? 8'h6f : _GEN_155; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_157 = 2'h1 == msgSel & 6'h19 == idx ? 8'h74 : _GEN_156; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_158 = 2'h1 == msgSel & 6'h1a == idx ? 8'h20 : _GEN_157; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_159 = 2'h1 == msgSel & 6'h1b == idx ? 8'h49 : _GEN_158; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_160 = 2'h1 == msgSel & 6'h1c == idx ? 8'h6d : _GEN_159; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_161 = 2'h1 == msgSel & 6'h1d == idx ? 8'h70 : _GEN_160; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_162 = 2'h1 == msgSel & 6'h1e == idx ? 8'h6c : _GEN_161; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_163 = 2'h1 == msgSel & 6'h1f == idx ? 8'h65 : _GEN_162; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_164 = 2'h1 == msgSel & 6'h20 == idx ? 8'h6d : _GEN_163; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_165 = 2'h1 == msgSel & 6'h21 == idx ? 8'h65 : _GEN_164; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_166 = 2'h1 == msgSel & 6'h22 == idx ? 8'h6e : _GEN_165; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_167 = 2'h1 == msgSel & 6'h23 == idx ? 8'h74 : _GEN_166; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_168 = 2'h1 == msgSel & 6'h24 == idx ? 8'h65 : _GEN_167; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_169 = 2'h1 == msgSel & 6'h25 == idx ? 8'h64 : _GEN_168; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_170 = 2'h1 == msgSel & 6'h26 == idx ? 8'h20 : _GEN_169; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_171 = 2'h1 == msgSel & 6'h27 == idx ? 8'h59 : _GEN_170; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_172 = 2'h1 == msgSel & 6'h28 == idx ? 8'h65 : _GEN_171; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_173 = 2'h1 == msgSel & 6'h29 == idx ? 8'h74 : _GEN_172; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_174 = 2'h1 == msgSel & 6'h2a == idx ? 8'h21 : _GEN_173; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_175 = 2'h1 == msgSel & 6'h2b == idx ? 8'h29 : _GEN_174; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_176 = 2'h1 == msgSel & 6'h2c == idx ? 8'hd : _GEN_175; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_177 = 2'h1 == msgSel & 6'h2d == idx ? 8'ha : _GEN_176; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_178 = 2'h1 == msgSel & 6'h2e == idx ? 8'h20 : _GEN_177; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_179 = 2'h1 == msgSel & 6'h2f == idx ? 8'h20 : _GEN_178; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_180 = 2'h1 == msgSel & 6'h30 == idx ? 8'h20 : _GEN_179; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_181 = 2'h1 == msgSel & 6'h31 == idx ? 8'h20 : _GEN_180; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_182 = 2'h1 == msgSel & 6'h32 == idx ? 8'h20 : _GEN_181; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_183 = 2'h1 == msgSel & 6'h33 == idx ? 8'h20 : _GEN_182; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_184 = 2'h1 == msgSel & 6'h34 == idx ? 8'h20 : _GEN_183; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_185 = 2'h1 == msgSel & 6'h35 == idx ? 8'h20 : _GEN_184; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_186 = 2'h1 == msgSel & 6'h36 == idx ? 8'h20 : _GEN_185; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_187 = 2'h1 == msgSel & 6'h37 == idx ? 8'h20 : _GEN_186; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_188 = 2'h1 == msgSel & 6'h38 == idx ? 8'h20 : _GEN_187; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_189 = 2'h1 == msgSel & 6'h39 == idx ? 8'h20 : _GEN_188; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_190 = 2'h1 == msgSel & 6'h3a == idx ? 8'h20 : _GEN_189; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_191 = 2'h1 == msgSel & 6'h3b == idx ? 8'h20 : _GEN_190; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_192 = 2'h1 == msgSel & 6'h3c == idx ? 8'h20 : _GEN_191; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_193 = 2'h1 == msgSel & 6'h3d == idx ? 8'h20 : _GEN_192; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_194 = 2'h1 == msgSel & 6'h3e == idx ? 8'h20 : _GEN_193; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_195 = 2'h1 == msgSel & 6'h3f == idx ? 8'h20 : _GEN_194; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_196 = 2'h2 == msgSel & 6'h0 == idx ? rxStored_0 : _GEN_195; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_197 = 2'h2 == msgSel & 6'h1 == idx ? rxStored_1 : _GEN_196; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_198 = 2'h2 == msgSel & 6'h2 == idx ? rxStored_2 : _GEN_197; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_199 = 2'h2 == msgSel & 6'h3 == idx ? rxStored_3 : _GEN_198; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_200 = 2'h2 == msgSel & 6'h4 == idx ? rxStored_4 : _GEN_199; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_201 = 2'h2 == msgSel & 6'h5 == idx ? rxStored_5 : _GEN_200; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_202 = 2'h2 == msgSel & 6'h6 == idx ? rxStored_6 : _GEN_201; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_203 = 2'h2 == msgSel & 6'h7 == idx ? rxStored_7 : _GEN_202; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_204 = 2'h2 == msgSel & 6'h8 == idx ? rxStored_8 : _GEN_203; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_205 = 2'h2 == msgSel & 6'h9 == idx ? rxStored_9 : _GEN_204; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_206 = 2'h2 == msgSel & 6'ha == idx ? rxStored_10 : _GEN_205; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_207 = 2'h2 == msgSel & 6'hb == idx ? rxStored_11 : _GEN_206; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_208 = 2'h2 == msgSel & 6'hc == idx ? rxStored_12 : _GEN_207; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_209 = 2'h2 == msgSel & 6'hd == idx ? rxStored_13 : _GEN_208; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_210 = 2'h2 == msgSel & 6'he == idx ? rxStored_14 : _GEN_209; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_211 = 2'h2 == msgSel & 6'hf == idx ? rxStored_15 : _GEN_210; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_212 = 2'h2 == msgSel & 6'h10 == idx ? rxStored_16 : _GEN_211; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_213 = 2'h2 == msgSel & 6'h11 == idx ? rxStored_17 : _GEN_212; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_214 = 2'h2 == msgSel & 6'h12 == idx ? rxStored_18 : _GEN_213; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_215 = 2'h2 == msgSel & 6'h13 == idx ? rxStored_19 : _GEN_214; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_216 = 2'h2 == msgSel & 6'h14 == idx ? rxStored_20 : _GEN_215; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_217 = 2'h2 == msgSel & 6'h15 == idx ? rxStored_21 : _GEN_216; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_218 = 2'h2 == msgSel & 6'h16 == idx ? rxStored_22 : _GEN_217; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_219 = 2'h2 == msgSel & 6'h17 == idx ? rxStored_23 : _GEN_218; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_220 = 2'h2 == msgSel & 6'h18 == idx ? rxStored_24 : _GEN_219; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_221 = 2'h2 == msgSel & 6'h19 == idx ? rxStored_25 : _GEN_220; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_222 = 2'h2 == msgSel & 6'h1a == idx ? rxStored_26 : _GEN_221; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_223 = 2'h2 == msgSel & 6'h1b == idx ? rxStored_27 : _GEN_222; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_224 = 2'h2 == msgSel & 6'h1c == idx ? rxStored_28 : _GEN_223; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_225 = 2'h2 == msgSel & 6'h1d == idx ? rxStored_29 : _GEN_224; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_226 = 2'h2 == msgSel & 6'h1e == idx ? rxStored_30 : _GEN_225; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_227 = 2'h2 == msgSel & 6'h1f == idx ? rxStored_31 : _GEN_226; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_228 = 2'h2 == msgSel & 6'h20 == idx ? 8'h0 : _GEN_227; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_229 = 2'h2 == msgSel & 6'h21 == idx ? 8'h0 : _GEN_228; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_230 = 2'h2 == msgSel & 6'h22 == idx ? 8'h0 : _GEN_229; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_231 = 2'h2 == msgSel & 6'h23 == idx ? 8'h0 : _GEN_230; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_232 = 2'h2 == msgSel & 6'h24 == idx ? 8'h0 : _GEN_231; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_233 = 2'h2 == msgSel & 6'h25 == idx ? 8'h0 : _GEN_232; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_234 = 2'h2 == msgSel & 6'h26 == idx ? 8'h0 : _GEN_233; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_235 = 2'h2 == msgSel & 6'h27 == idx ? 8'h0 : _GEN_234; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_236 = 2'h2 == msgSel & 6'h28 == idx ? 8'h0 : _GEN_235; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_237 = 2'h2 == msgSel & 6'h29 == idx ? 8'h0 : _GEN_236; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_238 = 2'h2 == msgSel & 6'h2a == idx ? 8'h0 : _GEN_237; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_239 = 2'h2 == msgSel & 6'h2b == idx ? 8'h0 : _GEN_238; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_240 = 2'h2 == msgSel & 6'h2c == idx ? 8'h0 : _GEN_239; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_241 = 2'h2 == msgSel & 6'h2d == idx ? 8'h0 : _GEN_240; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_242 = 2'h2 == msgSel & 6'h2e == idx ? 8'h0 : _GEN_241; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_243 = 2'h2 == msgSel & 6'h2f == idx ? 8'h0 : _GEN_242; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_244 = 2'h2 == msgSel & 6'h30 == idx ? 8'h0 : _GEN_243; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_245 = 2'h2 == msgSel & 6'h31 == idx ? 8'h0 : _GEN_244; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_246 = 2'h2 == msgSel & 6'h32 == idx ? 8'h0 : _GEN_245; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_247 = 2'h2 == msgSel & 6'h33 == idx ? 8'h0 : _GEN_246; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_248 = 2'h2 == msgSel & 6'h34 == idx ? 8'h0 : _GEN_247; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_249 = 2'h2 == msgSel & 6'h35 == idx ? 8'h0 : _GEN_248; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_250 = 2'h2 == msgSel & 6'h36 == idx ? 8'h0 : _GEN_249; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_251 = 2'h2 == msgSel & 6'h37 == idx ? 8'h0 : _GEN_250; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_252 = 2'h2 == msgSel & 6'h38 == idx ? 8'h0 : _GEN_251; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_253 = 2'h2 == msgSel & 6'h39 == idx ? 8'h0 : _GEN_252; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_254 = 2'h2 == msgSel & 6'h3a == idx ? 8'h0 : _GEN_253; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_255 = 2'h2 == msgSel & 6'h3b == idx ? 8'h0 : _GEN_254; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_256 = 2'h2 == msgSel & 6'h3c == idx ? 8'h0 : _GEN_255; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_257 = 2'h2 == msgSel & 6'h3d == idx ? 8'h0 : _GEN_256; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_258 = 2'h2 == msgSel & 6'h3e == idx ? 8'h0 : _GEN_257; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_259 = 2'h2 == msgSel & 6'h3f == idx ? 8'h0 : _GEN_258; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_260 = 2'h3 == msgSel & 6'h0 == idx ? 8'h49 : _GEN_259; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_261 = 2'h3 == msgSel & 6'h1 == idx ? 8'h6e : _GEN_260; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_262 = 2'h3 == msgSel & 6'h2 == idx ? 8'h76 : _GEN_261; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_263 = 2'h3 == msgSel & 6'h3 == idx ? 8'h61 : _GEN_262; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_264 = 2'h3 == msgSel & 6'h4 == idx ? 8'h6c : _GEN_263; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_265 = 2'h3 == msgSel & 6'h5 == idx ? 8'h69 : _GEN_264; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_266 = 2'h3 == msgSel & 6'h6 == idx ? 8'h64 : _GEN_265; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_267 = 2'h3 == msgSel & 6'h7 == idx ? 8'h20 : _GEN_266; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_268 = 2'h3 == msgSel & 6'h8 == idx ? 8'h52 : _GEN_267; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_269 = 2'h3 == msgSel & 6'h9 == idx ? 8'h65 : _GEN_268; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_270 = 2'h3 == msgSel & 6'ha == idx ? 8'h73 : _GEN_269; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_271 = 2'h3 == msgSel & 6'hb == idx ? 8'h70 : _GEN_270; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_272 = 2'h3 == msgSel & 6'hc == idx ? 8'h6f : _GEN_271; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_273 = 2'h3 == msgSel & 6'hd == idx ? 8'h6e : _GEN_272; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_274 = 2'h3 == msgSel & 6'he == idx ? 8'h73 : _GEN_273; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_275 = 2'h3 == msgSel & 6'hf == idx ? 8'h65 : _GEN_274; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_276 = 2'h3 == msgSel & 6'h10 == idx ? 8'h20 : _GEN_275; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_277 = 2'h3 == msgSel & 6'h11 == idx ? 8'h54 : _GEN_276; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_278 = 2'h3 == msgSel & 6'h12 == idx ? 8'h79 : _GEN_277; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_279 = 2'h3 == msgSel & 6'h13 == idx ? 8'h70 : _GEN_278; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_280 = 2'h3 == msgSel & 6'h14 == idx ? 8'h65 : _GEN_279; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_281 = 2'h3 == msgSel & 6'h15 == idx ? 8'h21 : _GEN_280; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_282 = 2'h3 == msgSel & 6'h16 == idx ? 8'hd : _GEN_281; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_283 = 2'h3 == msgSel & 6'h17 == idx ? 8'ha : _GEN_282; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_284 = 2'h3 == msgSel & 6'h18 == idx ? 8'h20 : _GEN_283; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_285 = 2'h3 == msgSel & 6'h19 == idx ? 8'h20 : _GEN_284; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_286 = 2'h3 == msgSel & 6'h1a == idx ? 8'h20 : _GEN_285; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_287 = 2'h3 == msgSel & 6'h1b == idx ? 8'h20 : _GEN_286; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_288 = 2'h3 == msgSel & 6'h1c == idx ? 8'h20 : _GEN_287; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_289 = 2'h3 == msgSel & 6'h1d == idx ? 8'h20 : _GEN_288; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_290 = 2'h3 == msgSel & 6'h1e == idx ? 8'h20 : _GEN_289; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_291 = 2'h3 == msgSel & 6'h1f == idx ? 8'h20 : _GEN_290; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_292 = 2'h3 == msgSel & 6'h20 == idx ? 8'h20 : _GEN_291; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_293 = 2'h3 == msgSel & 6'h21 == idx ? 8'h20 : _GEN_292; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_294 = 2'h3 == msgSel & 6'h22 == idx ? 8'h20 : _GEN_293; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_295 = 2'h3 == msgSel & 6'h23 == idx ? 8'h20 : _GEN_294; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_296 = 2'h3 == msgSel & 6'h24 == idx ? 8'h20 : _GEN_295; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_297 = 2'h3 == msgSel & 6'h25 == idx ? 8'h20 : _GEN_296; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_298 = 2'h3 == msgSel & 6'h26 == idx ? 8'h20 : _GEN_297; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_299 = 2'h3 == msgSel & 6'h27 == idx ? 8'h20 : _GEN_298; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_300 = 2'h3 == msgSel & 6'h28 == idx ? 8'h20 : _GEN_299; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_301 = 2'h3 == msgSel & 6'h29 == idx ? 8'h20 : _GEN_300; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_302 = 2'h3 == msgSel & 6'h2a == idx ? 8'h20 : _GEN_301; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_303 = 2'h3 == msgSel & 6'h2b == idx ? 8'h20 : _GEN_302; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_304 = 2'h3 == msgSel & 6'h2c == idx ? 8'h20 : _GEN_303; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_305 = 2'h3 == msgSel & 6'h2d == idx ? 8'h20 : _GEN_304; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_306 = 2'h3 == msgSel & 6'h2e == idx ? 8'h20 : _GEN_305; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_307 = 2'h3 == msgSel & 6'h2f == idx ? 8'h20 : _GEN_306; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_308 = 2'h3 == msgSel & 6'h30 == idx ? 8'h20 : _GEN_307; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_309 = 2'h3 == msgSel & 6'h31 == idx ? 8'h20 : _GEN_308; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_310 = 2'h3 == msgSel & 6'h32 == idx ? 8'h20 : _GEN_309; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_311 = 2'h3 == msgSel & 6'h33 == idx ? 8'h20 : _GEN_310; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_312 = 2'h3 == msgSel & 6'h34 == idx ? 8'h20 : _GEN_311; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_313 = 2'h3 == msgSel & 6'h35 == idx ? 8'h20 : _GEN_312; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_314 = 2'h3 == msgSel & 6'h36 == idx ? 8'h20 : _GEN_313; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_315 = 2'h3 == msgSel & 6'h37 == idx ? 8'h20 : _GEN_314; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_316 = 2'h3 == msgSel & 6'h38 == idx ? 8'h20 : _GEN_315; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_317 = 2'h3 == msgSel & 6'h39 == idx ? 8'h20 : _GEN_316; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_318 = 2'h3 == msgSel & 6'h3a == idx ? 8'h20 : _GEN_317; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_319 = 2'h3 == msgSel & 6'h3b == idx ? 8'h20 : _GEN_318; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_320 = 2'h3 == msgSel & 6'h3c == idx ? 8'h20 : _GEN_319; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_321 = 2'h3 == msgSel & 6'h3d == idx ? 8'h20 : _GEN_320; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_322 = 2'h3 == msgSel & 6'h3e == idx ? 8'h20 : _GEN_321; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [7:0] _GEN_323 = 2'h3 == msgSel & 6'h3f == idx ? 8'h20 : _GEN_322; // @[\\src\\main\\scala\\Uart.scala 98:{25,25}]
  wire [5:0] _T_7 = rxCount - 6'h1; // @[\\src\\main\\scala\\Uart.scala 101:28]
  wire [5:0] _idx_T_1 = idx + 6'h1; // @[\\src\\main\\scala\\Uart.scala 104:20]
  Rx rxm ( // @[\\src\\main\\scala\\Uart.scala 22:19]
    .clock(rxm_clock),
    .reset(rxm_reset),
    .io_rxd(rxm_io_rxd),
    .io_channel_ready(rxm_io_channel_ready),
    .io_channel_valid(rxm_io_channel_valid),
    .io_channel_bits(rxm_io_channel_bits)
  );
  BufferedTx txm ( // @[\\src\\main\\scala\\Uart.scala 23:19]
    .clock(txm_clock),
    .reset(txm_reset),
    .io_txd(txm_io_txd),
    .io_channel_ready(txm_io_channel_ready),
    .io_channel_valid(txm_io_channel_valid),
    .io_channel_bits(txm_io_channel_bits)
  );
  Queue inQ ( // @[\\src\\main\\scala\\Uart.scala 49:19]
    .clock(inQ_clock),
    .reset(inQ_reset),
    .io_enq_ready(inQ_io_enq_ready),
    .io_enq_valid(inQ_io_enq_valid)
  );
  assign io_tx = txm_io_txd; // @[\\src\\main\\scala\\Uart.scala 31:23]
  assign rxm_clock = clock;
  assign rxm_reset = reset;
  assign rxm_io_rxd = io_rx; // @[\\src\\main\\scala\\Uart.scala 30:14]
  assign rxm_io_channel_ready = io_captureEnable ? inQ_io_enq_ready : 1'h1; // @[\\src\\main\\scala\\Uart.scala 53:30]
  assign txm_clock = clock;
  assign txm_reset = reset;
  assign txm_io_channel_valid = sending; // @[\\src\\main\\scala\\Uart.scala 96:17 83:24 97:26]
  assign txm_io_channel_bits = sending ? _GEN_323 : 8'h0; // @[\\src\\main\\scala\\Uart.scala 96:17 84:23 98:25]
  assign inQ_clock = clock;
  assign inQ_reset = reset;
  assign inQ_io_enq_valid = rxm_io_channel_valid & io_captureEnable; // @[\\src\\main\\scala\\Uart.scala 51:44]
  always @(posedge clock) begin
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_0 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h0 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_0 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_1 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_1 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_2 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h2 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_2 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_3 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h3 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_3 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_4 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h4 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_4 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_5 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h5 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_5 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_6 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h6 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_6 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_7 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h7 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_7 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_8 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h8 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_8 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_9 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h9 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_9 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_10 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'ha == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_10 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_11 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'hb == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_11 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_12 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'hc == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_12 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_13 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'hd == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_13 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_14 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'he == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_14 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_15 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'hf == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_15 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_16 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h10 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_16 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_17 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h11 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_17 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_18 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h12 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_18 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_19 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h13 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_19 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_20 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h14 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_20 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_21 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h15 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_21 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_22 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h16 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_22 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_23 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h17 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_23 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_24 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h18 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_24 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_25 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h19 == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_25 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_26 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1a == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_26 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_27 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1b == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_27 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_28 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1c == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_28 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_29 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1d == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_29 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_30 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1e == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_30 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 25:25]
      rxStored_31 <= 8'h0; // @[\\src\\main\\scala\\Uart.scala 25:25]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      if (5'h1f == rxCount[4:0]) begin // @[\\src\\main\\scala\\Uart.scala 44:23]
        rxStored_31 <= _rxStored_T_3; // @[\\src\\main\\scala\\Uart.scala 44:23]
      end
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 26:24]
      rxCount <= 6'h0; // @[\\src\\main\\scala\\Uart.scala 26:24]
    end else if (io_captureEnable & rxFire & rxCount < 6'h20) begin // @[\\src\\main\\scala\\Uart.scala 43:54]
      rxCount <= _rxCount_T_1; // @[\\src\\main\\scala\\Uart.scala 45:13]
    end else if (io_clearBuffer) begin // @[\\src\\main\\scala\\Uart.scala 39:24]
      rxCount <= 6'h0; // @[\\src\\main\\scala\\Uart.scala 40:13]
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 75:20]
      idx <= 6'h0; // @[\\src\\main\\scala\\Uart.scala 75:20]
    end else if (sending) begin // @[\\src\\main\\scala\\Uart.scala 96:17]
      if (txm_io_channel_ready) begin // @[\\src\\main\\scala\\Uart.scala 100:32]
        if (idx == _T_7) begin // @[\\src\\main\\scala\\Uart.scala 101:35]
          idx <= _GEN_67;
        end else begin
          idx <= _idx_T_1; // @[\\src\\main\\scala\\Uart.scala 104:13]
        end
      end else begin
        idx <= _GEN_67;
      end
    end else begin
      idx <= _GEN_67;
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 76:24]
      sending <= 1'h0; // @[\\src\\main\\scala\\Uart.scala 76:24]
    end else if (sending) begin // @[\\src\\main\\scala\\Uart.scala 96:17]
      if (txm_io_channel_ready) begin // @[\\src\\main\\scala\\Uart.scala 100:32]
        if (idx == _T_7) begin // @[\\src\\main\\scala\\Uart.scala 101:35]
          sending <= 1'h0; // @[\\src\\main\\scala\\Uart.scala 102:17]
        end else begin
          sending <= _GEN_66;
        end
      end else begin
        sending <= _GEN_66;
      end
    end else begin
      sending <= _GEN_66;
    end
    if (reset) begin // @[\\src\\main\\scala\\Uart.scala 79:25]
      sendPrev <= 1'h0; // @[\\src\\main\\scala\\Uart.scala 79:25]
    end else begin
      sendPrev <= io_sendResponse; // @[\\src\\main\\scala\\Uart.scala 79:25]
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
  rxStored_0 = _RAND_0[7:0];
  _RAND_1 = {1{`RANDOM}};
  rxStored_1 = _RAND_1[7:0];
  _RAND_2 = {1{`RANDOM}};
  rxStored_2 = _RAND_2[7:0];
  _RAND_3 = {1{`RANDOM}};
  rxStored_3 = _RAND_3[7:0];
  _RAND_4 = {1{`RANDOM}};
  rxStored_4 = _RAND_4[7:0];
  _RAND_5 = {1{`RANDOM}};
  rxStored_5 = _RAND_5[7:0];
  _RAND_6 = {1{`RANDOM}};
  rxStored_6 = _RAND_6[7:0];
  _RAND_7 = {1{`RANDOM}};
  rxStored_7 = _RAND_7[7:0];
  _RAND_8 = {1{`RANDOM}};
  rxStored_8 = _RAND_8[7:0];
  _RAND_9 = {1{`RANDOM}};
  rxStored_9 = _RAND_9[7:0];
  _RAND_10 = {1{`RANDOM}};
  rxStored_10 = _RAND_10[7:0];
  _RAND_11 = {1{`RANDOM}};
  rxStored_11 = _RAND_11[7:0];
  _RAND_12 = {1{`RANDOM}};
  rxStored_12 = _RAND_12[7:0];
  _RAND_13 = {1{`RANDOM}};
  rxStored_13 = _RAND_13[7:0];
  _RAND_14 = {1{`RANDOM}};
  rxStored_14 = _RAND_14[7:0];
  _RAND_15 = {1{`RANDOM}};
  rxStored_15 = _RAND_15[7:0];
  _RAND_16 = {1{`RANDOM}};
  rxStored_16 = _RAND_16[7:0];
  _RAND_17 = {1{`RANDOM}};
  rxStored_17 = _RAND_17[7:0];
  _RAND_18 = {1{`RANDOM}};
  rxStored_18 = _RAND_18[7:0];
  _RAND_19 = {1{`RANDOM}};
  rxStored_19 = _RAND_19[7:0];
  _RAND_20 = {1{`RANDOM}};
  rxStored_20 = _RAND_20[7:0];
  _RAND_21 = {1{`RANDOM}};
  rxStored_21 = _RAND_21[7:0];
  _RAND_22 = {1{`RANDOM}};
  rxStored_22 = _RAND_22[7:0];
  _RAND_23 = {1{`RANDOM}};
  rxStored_23 = _RAND_23[7:0];
  _RAND_24 = {1{`RANDOM}};
  rxStored_24 = _RAND_24[7:0];
  _RAND_25 = {1{`RANDOM}};
  rxStored_25 = _RAND_25[7:0];
  _RAND_26 = {1{`RANDOM}};
  rxStored_26 = _RAND_26[7:0];
  _RAND_27 = {1{`RANDOM}};
  rxStored_27 = _RAND_27[7:0];
  _RAND_28 = {1{`RANDOM}};
  rxStored_28 = _RAND_28[7:0];
  _RAND_29 = {1{`RANDOM}};
  rxStored_29 = _RAND_29[7:0];
  _RAND_30 = {1{`RANDOM}};
  rxStored_30 = _RAND_30[7:0];
  _RAND_31 = {1{`RANDOM}};
  rxStored_31 = _RAND_31[7:0];
  _RAND_32 = {1{`RANDOM}};
  rxCount = _RAND_32[5:0];
  _RAND_33 = {1{`RANDOM}};
  idx = _RAND_33[5:0];
  _RAND_34 = {1{`RANDOM}};
  sending = _RAND_34[0:0];
  _RAND_35 = {1{`RANDOM}};
  sendPrev = _RAND_35[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module RISCV(
  input   clock,
  input   reset,
  input   io_rx, // @[\\src\\main\\scala\\main.scala 6:14]
  input   io_readyToReadProgram, // @[\\src\\main\\scala\\main.scala 6:14]
  input   io_runProgram, // @[\\src\\main\\scala\\main.scala 6:14]
  output  io_tx, // @[\\src\\main\\scala\\main.scala 6:14]
  output  io_idleLED, // @[\\src\\main\\scala\\main.scala 6:14]
  output  io_listeningLED, // @[\\src\\main\\scala\\main.scala 6:14]
  output  io_runningLED // @[\\src\\main\\scala\\main.scala 6:14]
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
`endif // RANDOMIZE_REG_INIT
  wire  uart_clock; // @[\\src\\main\\scala\\main.scala 59:20]
  wire  uart_reset; // @[\\src\\main\\scala\\main.scala 59:20]
  wire  uart_io_rx; // @[\\src\\main\\scala\\main.scala 59:20]
  wire  uart_io_sendResponse; // @[\\src\\main\\scala\\main.scala 59:20]
  wire [3:0] uart_io_responseType; // @[\\src\\main\\scala\\main.scala 59:20]
  wire  uart_io_captureEnable; // @[\\src\\main\\scala\\main.scala 59:20]
  wire  uart_io_clearBuffer; // @[\\src\\main\\scala\\main.scala 59:20]
  wire  uart_io_tx; // @[\\src\\main\\scala\\main.scala 59:20]
  reg [1:0] state; // @[\\src\\main\\scala\\main.scala 28:22]
  reg [3:0] uartResponseType; // @[\\src\\main\\scala\\main.scala 31:33]
  reg  readyPrev; // @[\\src\\main\\scala\\main.scala 34:26]
  reg  runPrev; // @[\\src\\main\\scala\\main.scala 35:24]
  wire  readyRise = io_readyToReadProgram & ~readyPrev; // @[\\src\\main\\scala\\main.scala 37:41]
  wire  runRise = io_runProgram & ~runPrev; // @[\\src\\main\\scala\\main.scala 38:31]
  reg  captureActive; // @[\\src\\main\\scala\\main.scala 44:30]
  wire  _GEN_0 = readyRise | captureActive; // @[\\src\\main\\scala\\main.scala 45:19 46:19 44:30]
  wire  _GEN_2057 = 2'h2 == state ? 1'h0 : 2'h1 == state; // @[\\src\\main\\scala\\main.scala 82:17 91:21]
  Uart uart ( // @[\\src\\main\\scala\\main.scala 59:20]
    .clock(uart_clock),
    .reset(uart_reset),
    .io_rx(uart_io_rx),
    .io_sendResponse(uart_io_sendResponse),
    .io_responseType(uart_io_responseType),
    .io_captureEnable(uart_io_captureEnable),
    .io_clearBuffer(uart_io_clearBuffer),
    .io_tx(uart_io_tx)
  );
  assign io_tx = uart_io_tx; // @[\\src\\main\\scala\\main.scala 74:9]
  assign io_idleLED = 2'h0 == state; // @[\\src\\main\\scala\\main.scala 82:17]
  assign io_listeningLED = 2'h0 == state ? 1'h0 : 2'h2 == state; // @[\\src\\main\\scala\\main.scala 82:17 85:23]
  assign io_runningLED = 2'h0 == state ? 1'h0 : _GEN_2057; // @[\\src\\main\\scala\\main.scala 82:17 86:21]
  assign uart_clock = clock;
  assign uart_reset = reset;
  assign uart_io_rx = io_rx; // @[\\src\\main\\scala\\main.scala 60:14]
  assign uart_io_sendResponse = io_runProgram; // @[\\src\\main\\scala\\main.scala 62:24]
  assign uart_io_responseType = uartResponseType; // @[\\src\\main\\scala\\main.scala 61:24]
  assign uart_io_captureEnable = captureActive; // @[\\src\\main\\scala\\main.scala 63:25]
  assign uart_io_clearBuffer = io_readyToReadProgram & ~readyPrev; // @[\\src\\main\\scala\\main.scala 37:41]
  always @(posedge clock) begin
    if (reset) begin // @[\\src\\main\\scala\\main.scala 28:22]
      state <= 2'h0; // @[\\src\\main\\scala\\main.scala 28:22]
    end else if (io_runProgram) begin // @[\\src\\main\\scala\\main.scala 53:23]
      state <= 2'h1; // @[\\src\\main\\scala\\main.scala 55:11]
    end else if (readyRise) begin // @[\\src\\main\\scala\\main.scala 45:19]
      state <= 2'h2; // @[\\src\\main\\scala\\main.scala 47:11]
    end
    if (reset) begin // @[\\src\\main\\scala\\main.scala 31:33]
      uartResponseType <= 4'h0; // @[\\src\\main\\scala\\main.scala 31:33]
    end else if (io_runProgram) begin // @[\\src\\main\\scala\\main.scala 53:23]
      uartResponseType <= 4'h2; // @[\\src\\main\\scala\\main.scala 54:22]
    end
    if (reset) begin // @[\\src\\main\\scala\\main.scala 34:26]
      readyPrev <= 1'h0; // @[\\src\\main\\scala\\main.scala 34:26]
    end else begin
      readyPrev <= io_readyToReadProgram; // @[\\src\\main\\scala\\main.scala 40:13]
    end
    if (reset) begin // @[\\src\\main\\scala\\main.scala 35:24]
      runPrev <= 1'h0; // @[\\src\\main\\scala\\main.scala 35:24]
    end else begin
      runPrev <= io_runProgram; // @[\\src\\main\\scala\\main.scala 41:11]
    end
    if (reset) begin // @[\\src\\main\\scala\\main.scala 44:30]
      captureActive <= 1'h0; // @[\\src\\main\\scala\\main.scala 44:30]
    end else if (runRise) begin // @[\\src\\main\\scala\\main.scala 49:17]
      captureActive <= 1'h0; // @[\\src\\main\\scala\\main.scala 50:19]
    end else begin
      captureActive <= _GEN_0;
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
  state = _RAND_0[1:0];
  _RAND_1 = {1{`RANDOM}};
  uartResponseType = _RAND_1[3:0];
  _RAND_2 = {1{`RANDOM}};
  readyPrev = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  runPrev = _RAND_3[0:0];
  _RAND_4 = {1{`RANDOM}};
  captureActive = _RAND_4[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
