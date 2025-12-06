import socket
from typing import Tuple

def scan_port(host: str, port: int, timeout: float=0.5) -> Tuple[int, bool]:
    """
    Attempt to connect to a specific port on the target IP.

    :param host: Target hostname or IP
    :param port: The port to scan.
    :param timeout: Socket timeout in seconds
    :return: Tuple[int, bool]: (port, is_open)
    """
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(timeout)

    try:
        result = sock.connect_ex((host, port))
        return port, (result == 0)
    except Exception:
        return port, False
    finally:
        sock.close()

def scan_range(host: str, start_port: int, end_port: int) -> None:
    """
    Scan a range of ports on the given host and print results.

    :param host: Target domain or IP
    :param start_port: starting port
    :param end_port: ending port
    """
    try:
        ip = socket.gethostbyname(host)
    except socket.gaierror:
        print(f"[ERROR] Unable to resolve host: {host}")
        return

    print(f"Scanning host: {host} ({ip})")
    print("--------------------------------")

    for port in range(start_port, end_port + 1):
        _, is_open = scan_port(ip, port)
        status = "OPEN" if is_open else "CLOSED"
        if is_open:
            print(f"{port} {status}")
    print(f"(Others) CLOSED")
