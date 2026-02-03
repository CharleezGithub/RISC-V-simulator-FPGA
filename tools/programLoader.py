import argparse
import re
import struct
import time
from pathlib import Path

import serial

BIN_FOLDER = Path("tools/programTests")
SEND_DELAY = 0.01  # delay between chunks (seconds)
CHUNK_SIZE = 64  # bytes per write
MAX_WORDS = 512
REG_COUNT = 32
HEX_LINE_RE = re.compile(r"^0x[0-9a-fA-F]{8}$")


def list_bin_files():
    bins = sorted(BIN_FOLDER.glob("*.bin"))
    if not bins:
        print("No .bin files found in", BIN_FOLDER)
        return []
    return bins


def select_bin_file(bins):
    print("\nAvailable programs:")
    for i, b in enumerate(bins):
        print(f"  [{i}] {b.name}")

    while True:
        try:
            choice = int(input("\nSelect program number: "))
            if 0 <= choice < len(bins):
                return bins[choice]
        except ValueError:
            pass
        print("Invalid selection, try again.")


def send_bin(
    ser,
    path: Path,
    pad_to_words=None,
    skip_oversize=False,
    send_length_prefix=True,
):
    data = path.read_bytes()
    if len(data) % 4 != 0:
        print("Warning: file size not multiple of 4 bytes:", path.name)
        data += b"\x00" * (4 - (len(data) % 4))

    word_count = len(data) // 4

    if pad_to_words is not None:
        max_bytes = pad_to_words * 4
        if word_count > pad_to_words:
            msg = (
                f"{path.name} is too large ({len(data)} bytes) for {pad_to_words} words"
            )
            if skip_oversize:
                print("Skipping:", msg)
                return False
            raise ValueError(msg)
        if word_count < pad_to_words:
            data += b"\x00" * ((pad_to_words - word_count) * 4)
        word_count = pad_to_words
    elif word_count > MAX_WORDS:
        print(
            f"Warning: {path.name} is larger than {MAX_WORDS} words "
            f"({len(data)} bytes)."
        )

    payload = data
    if send_length_prefix:
        payload = struct.pack("<I", word_count) + data

    print(f"\nSending {path.name} ({len(payload)} bytes)")
    for i in range(0, len(payload), CHUNK_SIZE):
        chunk = payload[i : i + CHUNK_SIZE]
        ser.write(chunk)
        time.sleep(SEND_DELAY)

    print("Send complete.")
    return True


def data_parser(raw_data):
    try:
        text = raw_data.decode("ascii")

        out = []
        for line in text.splitlines():
            if not line:
                continue
            value = int(line, 16)
            out.append(f"0x{value:x}")
        return "\n".join(out)

    except Exception:
        print("Couldn't parse as ASCII, returning binary")
        return raw_data


def listen(ser):
    while True:
        n = ser.in_waiting
        if n > 0:
            data = ser.read(n)
            print("Received raw:", data)
            data_parsed = data_parser(data)
            print("Parsed Data:\n" + str(data_parsed))

        time.sleep(0.3)


def parse_res(path: Path):
    regs = []
    with open(path, "rb") as f:
        for _ in range(REG_COUNT):
            data = f.read(4)
            if len(data) != 4:
                raise ValueError(f"{path.name} is too short for {REG_COUNT} registers")
            regs.append(struct.unpack("<I", data)[0])
    return regs


def read_register_dump(ser, expected_count=REG_COUNT, overall_timeout=20.0, idle_timeout=0.3):
    prev_timeout = ser.timeout
    ser.timeout = 0.2
    try:
        buf = ""
        values = []
        start = time.monotonic()
        last_data = start

        while time.monotonic() - start < overall_timeout:
            chunk = ser.read(256)
            if chunk:
                last_data = time.monotonic()
                buf += chunk.decode("ascii", errors="ignore")
                while "\n" in buf:
                    line, buf = buf.split("\n", 1)
                    line = line.strip()
                    if HEX_LINE_RE.match(line):
                        values.append(int(line, 16))
            else:
                if len(values) >= expected_count and (time.monotonic() - last_data) > idle_timeout:
                    break

        if len(values) < expected_count:
            raise TimeoutError("Timed out waiting for register dump")

        return values
    finally:
        ser.timeout = prev_timeout


def compare_regs(expected, actual):
    mismatches = []
    for i, (exp, act) in enumerate(zip(expected, actual)):
        if exp != act:
            mismatches.append((i, exp, act))
    return mismatches


def pick_best_window(expected, received):
    window_len = len(expected)
    if len(received) < window_len:
        return None, None, None

    best_start = 0
    best_window = received[:window_len]
    best_mismatches = compare_regs(expected, best_window)
    if not best_mismatches:
        return best_start, best_window, best_mismatches

    for start in range(1, len(received) - window_len + 1):
        window = received[start : start + window_len]
        mismatches = compare_regs(expected, window)
        if len(mismatches) < len(best_mismatches):
            best_start = start
            best_window = window
            best_mismatches = mismatches
            if not best_mismatches:
                break

    return best_start, best_window, best_mismatches


def run_single(ser, send_length_prefix):
    bins = list_bin_files()
    if not bins:
        return

    selected = select_bin_file(bins)
    send_bin(ser, selected, send_length_prefix=send_length_prefix)

    print("Listening!")
    listen(ser)


def run_batch(ser, max_words, send_length_prefix, retries, retry_prompt):
    bins = list_bin_files()
    if not bins:
        return

    tests = []
    missing = []
    for b in bins:
        res = b.with_suffix(".res")
        if res.exists():
            tests.append((b, res))
        else:
            missing.append(b)

    if missing:
        print("Skipping tests without .res files:")
        for b in missing:
            print("  -", b.name)

    if not tests:
        print("No tests with .res files found.")
        return

    passed = 0
    failed = 0
    errors = 0
    oversize = 0

    for idx, (bin_path, res_path) in enumerate(tests, start=1):
        retries_left = max(0, retries)
        attempt = 1
        while True:
            header = f"\n=== [{idx}/{len(tests)}] {bin_path.name}"
            if retries > 0 or retry_prompt:
                header += f" (attempt {attempt}"
                if retries > 0:
                    header += f"/{retries + 1}"
                header += ")"
            header += " ==="
            print(header)

            input("Press READY/LOAD on the FPGA, then press Enter to send the program.")
            ser.reset_input_buffer()
            sent = send_bin(
                ser,
                bin_path,
                pad_to_words=max_words,
                skip_oversize=True,
                send_length_prefix=send_length_prefix,
            )
            if not sent:
                oversize += 1
                break

            print("Press RUN on the FPGA to execute the program.")
            input("Press Enter to arm register capture, then press PRINT REGS on the FPGA.")
            ser.reset_input_buffer()

            print("Waiting for register dump...")
            status = "pass"
            mismatches = []
            err_msg = ""
            try:
                actual = read_register_dump(ser)
            except TimeoutError as err:
                status = "error"
                err_msg = str(err)
            else:
                expected = parse_res(res_path)
                start_idx, window, mismatches = pick_best_window(expected, actual)
                if window is None:
                    status = "error"
                    err_msg = "Not enough register lines to compare."
                else:
                    if len(actual) != len(expected) or start_idx != 0:
                        print(
                            f"Captured {len(actual)} hex lines; using window starting at {start_idx}."
                        )
                    if mismatches:
                        status = "mismatch"

            if status == "pass":
                passed += 1
                print("PASS")
                break

            if status == "mismatch":
                print(f"FAIL: {len(mismatches)} mismatches")
                for reg_idx, exp, act in mismatches:
                    print(
                        f"  x{reg_idx:02}: expected 0x{exp:08x} got 0x{act:08x}"
                    )
            else:
                print("ERROR:", err_msg)

            if retries_left > 0:
                retries_left -= 1
                attempt += 1
                print("Retrying test...")
                continue

            if retry_prompt:
                choice = input("Retry this test? [y/N] ").strip().lower()
                if choice in ("y", "yes"):
                    attempt += 1
                    continue

            if status == "mismatch":
                failed += 1
            else:
                errors += 1
            break

    print("\nBatch complete.")
    print(
        "Passed: {passed}, Failed: {failed}, Errors: {errors}, "
        "Skipped (missing .res): {missing}, Skipped (oversize): {oversize}".format(
            passed=passed,
            failed=failed,
            errors=errors,
            missing=len(missing),
            oversize=oversize,
        )
    )


def main():
    parser = argparse.ArgumentParser(description="RISC-V FPGA program loader")
    parser.add_argument("--port", default="COM4", help="Serial port (default: COM4)")
    parser.add_argument("--baud", type=int, default=115200, help="Baud rate (default: 115200)")
    parser.add_argument("--timeout", type=float, default=10, help="Serial timeout in seconds (default: 10)")
    parser.add_argument(
        "--max-words",
        type=int,
        default=MAX_WORDS,
        help=f"Max instruction words for padding/skipping (default: {MAX_WORDS})",
    )
    parser.add_argument(
        "--no-length-prefix",
        action="store_true",
        help="Send raw .bin without length header (legacy bootloader)",
    )
    parser.add_argument(
        "--retries",
        type=int,
        default=0,
        help="Number of automatic retries per test when it fails (default: 0)",
    )
    parser.add_argument(
        "--no-retry-prompt",
        action="store_true",
        help="Do not prompt for manual retry when a test fails",
    )
    parser.add_argument(
        "--all",
        action="store_true",
        help="Send all tests with .res files and compare register dumps",
    )
    args = parser.parse_args()

    ser = serial.Serial(
        port=args.port,
        baudrate=args.baud,
        timeout=args.timeout,
    )

    try:
        send_length_prefix = not args.no_length_prefix
        if args.all:
            run_batch(
                ser,
                args.max_words,
                send_length_prefix,
                retries=args.retries,
                retry_prompt=not args.no_retry_prompt,
            )
        else:
            run_single(ser, send_length_prefix)
    finally:
        ser.close()


if __name__ == "__main__":
    main()
