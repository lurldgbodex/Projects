# Custom wc Command

This Python script serves as a personalized version of the Unix command-line tool `wc` (word count), providing functionality to count the number of bytes, lines, words, and characters in a file or input text.

## Usage

### Requirements

- Python 3.x

### Command Syntax

```bash
./ccwc [FLAG] [FILE]
```

### Flags

- `-c`: Count the number of bytes in the specified file or input text.
- `-l`: Count the number of lines in the specified file or input text.
- `-w`: Count the number of words in the specified file or input text.
- `-m`: Count the number of characters in the specified file or input text.

### Examples

#### Counting bytes in a file

```bash
./ccwc -c file.txt
```

#### Counting lines in a file

```bash
./ccwc -l file.txt
```

#### Counting words in a file

```bash
./ccwc -w file.txt
```

#### Counting characters in a file

```bash
./ccwc -m file.txt
```

#### Displaying line, word, and character counts in a file

```bash
./ccwc file.txt
```

### Input from Standard Input

To input text directly instead of specifying a file, you can pipe the text into the script. For example:

```bash
echo "This is a test." | ./ccwc -m
```

## Function Documentation

The script includes several functions to perform the counting tasks:

- `count_bytes(content)`: Count the number of bytes in the given content.
- `count_lines(content)`: Count the number of lines in the given content.
- `count_words(content)`: Count the number of words in the given content.
- `count_characters(content)`: Count the number of characters in the given content.
- `ccwc(flag, content)`: Core function to perform the counting based on the provided flag and content.
- `print_exception(message)`: Print error messages when exceptions occur.

## File Handling

The script reads content from files specified as command-line arguments or from standard input. It then processes the content based on the provided flag to perform the desired counting operation.

## Error Handling

The script handles exceptions that may occur during file operations or content processing. Error messages are printed to provide feedback to the user.

## Note

This script aims to provide a simple and personalized version of the `wc` command-line tool for counting purposes. Feel free to customize it further according to your specific needs or requirements.
