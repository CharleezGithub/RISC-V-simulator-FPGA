import subprocess
from pathlib import Path
import sys

GCC = "riscv64-unknown-elf-gcc"
OBJCOPY = "riscv64-unknown-elf-objcopy"


ARCH_FLAGS = ["-march=rv32i", "-mabi=ilp32"]

LINK_FLAGS = [
    "-nostdlib",
    "-Wl,--entry=_start",
]


def run(cmd):
    print(" ".join(cmd))
    subprocess.run(cmd, check=True)


def build_all(folder):
    folder = Path(folder)
    sources = list(folder.glob("*.s"))

    if not sources:
        print("No .s files found.")
        return

    for src in sources:
        elf = src.with_suffix(".elf")
        binfile = src.with_suffix(".bin")

        print(f"\n=== Building {src.name} ===")

        # Assemble & link
        run([GCC, *ARCH_FLAGS, *LINK_FLAGS, "-o", str(elf), str(src)])

        # ELF → BIN
        run([OBJCOPY, "-O", "binary", str(elf), str(binfile)])

        print(f"Built: {binfile.name}")


if __name__ == "__main__":
    # Get dir
    if len(sys.argv) != 2:
        print("Usage: python build_riscv_bin.py path/to/folder")
        sys.exit(1)

    try:
        build_all(sys.argv[1])
    except Exception as e:
        print("Error. Run inside WSL terminal!")
