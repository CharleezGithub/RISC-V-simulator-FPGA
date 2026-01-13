import serial
import time
import threading
from pathlib import Path

BIN_FOLDER = Path("tools/programTests")
SEND_DELAY = 0.01  # delay between chunks (seconds)
CHUNK_SIZE = 64  # bytes per write

# Open the serial port
ser = serial.Serial(
    port="COM4",
    baudrate=115200,
    timeout=10,  # seconds
)


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


def send_bin(path: Path):
    data = path.read_bytes()
    print(f"\nSending {path.name} ({len(data)} bytes)")

    for i in range(0, len(data), CHUNK_SIZE):
        chunk = data[i : i + CHUNK_SIZE]
        ser.write(chunk)
        time.sleep(SEND_DELAY)

    print("Send complete.")


def send(message):
    ser.write(message)
    print("Sent:", message)


# Read data
def listen():
    while True:
        n = ser.in_waiting
        if n > 0:
            data = ser.read(n)
            print("Received raw:", data)
            data_parsed = data_parser(data)
            print("Parsed Data:\n" + str(data_parsed))

        time.sleep(0.3)


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

    except Exception as err:
        print("Couldn't parse as ASCII, returning binary")
        return raw_data


# Close the port
def close():
    ser.close()


def send_loop(messages):
    for _ in range(5):
        for i in range(len(messages)):
            send(messages[i])
            time.sleep(1)


if __name__ == "__main__":
    bins = list_bin_files()
    if not bins:
        exit(1)

    selected = select_bin_file(bins)

    send_bin(selected)

    print("Listening!")
    listen()

    print("\nPress Ctrl+C to exit.")
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nClosing serial port.")
        close()
