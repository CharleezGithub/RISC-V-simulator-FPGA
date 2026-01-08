from pathlib import Path
import struct

BIN_FOLDER = Path("tools/programTests")


def list_bin_files():
    bins = sorted(BIN_FOLDER.glob("*.bin"))
    if not bins:
        print("No .bin files found in", BIN_FOLDER)
        exit(1)
    return bins


def select_bin_file(bins):
    print("\nAvailable .bin files:")
    for i, b in enumerate(bins):
        print(f"  [{i}] {b.name}")

    while True:
        try:
            idx = int(input("\nSelect file number: "))
            if 0 <= idx < len(bins):
                return bins[idx]
        except ValueError:
            pass
        print("Invalid selection.")


def load_words(path):
    data = path.read_bytes()
    if len(data) % 4 != 0:
        print("Warning: file size not multiple of 4 bytes")
    words = [
        struct.unpack("<I", data[i:i+4])[0]
        for i in range(0, len(data), 4)
    ]
    return words


def show_words(words):
    print("\nInstructions:")
    for i, w in enumerate(words):
        print(f"[{i:02}] 0x{w:08x}")


def parse_instruction(text):
    text = text.replace("_", "").lower()
    if text.startswith("0x"):
        return int(text, 16)
    if all(c in "01" for c in text) and len(text) == 32:
        return int(text, 2)
    raise ValueError("Invalid instruction format")


def save_words(path, words):
    data = b"".join(struct.pack("<I", w) for w in words)
    path.write_bytes(data)


# ---------------- MAIN ----------------
if __name__ == "__main__":
    bins = list_bin_files()
    path = select_bin_file(bins)

    words = load_words(path)
    show_words(words)

    try:
        idx = int(input("\nInstruction index to edit: "))
        if not (0 <= idx < len(words)):
            raise ValueError
    except ValueError:
        print("Invalid index.")
        exit(1)

    print("\nEnter new instruction as:")
    print("  - 32-bit binary (e.g. 00000000010100101000000000110011)")
    print("  - hex (e.g. 0x00528033)")

    try:
        new_instr = parse_instruction(input("> "))
    except ValueError as e:
        print(e)
        exit(1)

    old = words[idx]
    words[idx] = new_instr

    save_words(path, words)

    print("\nUpdated file:", path.name)
    print(f"  old: 0x{old:08x}")
    print(f"  new: 0x{new_instr:08x}")
