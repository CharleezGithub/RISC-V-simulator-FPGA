import struct


def parse_res(path):
    regs = {}
    with open(path, "rb") as f:
        for i in range(32):
            data = f.read(4)
            value = struct.unpack("<I", data)[0]  # little-endian uint32
            regs[f"x{i}"] = value
    return regs


gold = parse_res("tools/programTests/addpos.res")

for r in gold:
    print(f"{gold[r]:#x}")
