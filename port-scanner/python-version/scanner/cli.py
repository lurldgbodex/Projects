import argparse
import sys
from scanner.scanner import scan_range

def parse_arguments():
    """Parse command line arguments"""
    parser = argparse.ArgumentParser(
        description="Simple Python Port Scanner (v0.1)",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
            Examples:
            %(prog)s --host google.com --start 1 --end 1024
            %(prog)s -H 192.168.1.1 -s 20 -e 100
            %(prog)s --host example.com --ports 80, 443, 8080
        '''
    )

    parser.add_argument(
        '--host', '-H',
        required=True,
        help='Target host (IP address or domain name)'
    )

    parser.add_argument(
        '--start', '-s',
        type=int,
        default=1,
        help='Start port (default: 1)'
    )

    parser.add_argument(
        '--end', '-e',
        type=int,
        default=1024,
        help='End port (default: 1024)'
    )

    return parser.parse_args()

def main():
    """Main function"""
    args = parse_arguments()
    scan_range(args.host, args.start, args.end)
