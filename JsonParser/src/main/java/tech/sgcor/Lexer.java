package tech.sgcor;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String json;
    private int position;

    public Lexer(String json) {
        this.json = json;
        this.position = 0;
    }

    private void getNextChar() {
        position++;
    }

    private char getCurrentChar() {
        return json.charAt(position);
    }

    private void skipWhiteSpace() {
        while (position < json.length() && Character.isWhitespace(json.charAt(position))) {
            position++;
        }
    }

    private Token parse() {
        skipWhiteSpace();
        char currentChar = getCurrentChar();
        String currentCharString = String.valueOf(currentChar);

        switch (currentChar) {
            case '\0' -> {
                return new Token(TokenType.END_OF_JSON, currentCharString);
            }
            case '{' -> {
                return new Token(TokenType.LEFT_BRACE, currentCharString);
            }
            case '}' -> {
                return new Token(TokenType.RIGHT_BRACE, currentCharString);
            }
            case '[' -> {
                return new Token(TokenType.LEFT_BRACKET, currentCharString);
            }
            case ']' -> {
                return new Token(TokenType.RIGHT_BRACKET, currentCharString);
            }
            case ':' -> {
                return new Token(TokenType.COLON, currentCharString);
            }
            case ',' -> {
                return new Token(TokenType.COMMA, currentCharString);
            }
            case '"' -> {
                return parseString();
            }
            case 't', 'f' -> {
                return parseBoolean();
            }
            case 'n' -> {
                return parseNull();
            }
            default -> {
                if (Character.isDigit(currentChar) || currentChar == '-') {
                    return parseNumber();
                } else {
                    throw new IllegalArgumentException("Invalid JSON Provided");
                }
            }
        }
    }

    private Token parseNull() {
        StringBuilder charString = new StringBuilder();

        while (position < json.length()) {
            char currentChar = getCurrentChar();

            if (!Character.isAlphabetic(currentChar)) {
                break;
            } else {
                charString.append(currentChar);
                getNextChar();
            }
        }

        String value = charString.toString();

        if (value.equals("null")) {
            return new Token(TokenType.NULL, null);
        }
        throw new IllegalArgumentException("ParseNull - invalid json value passed: " + value);
    }

    private Token parseBoolean() {
        StringBuilder charString = new StringBuilder();

        while (position < json.length()) {
            char currentChar = getCurrentChar();

            if (!Character.isAlphabetic(currentChar)) {
                break;
            } else {
                charString.append(currentChar);
                getNextChar();
            }
        }

        String booleanString = charString.toString();
        if (booleanString.equals("true")) {
            return new Token(TokenType.BOOLEAN, true);
        } else if (booleanString.equals("false")) {
            return new Token(TokenType.BOOLEAN, false);
        } else {
            throw new IllegalArgumentException("ParseBoolean: invalid boolean value");
        }
    }

    private Token parseNumber() {
        StringBuilder charString = new StringBuilder();
        boolean hasDecimal = false;

        while (position < json.length()) {
            char currentChar = getCurrentChar();
            if (Character.isDigit(currentChar) || currentChar == '-') {
                charString.append(currentChar);
            } else if (currentChar == '.') {
                if (hasDecimal) {
                    throw  new IllegalArgumentException("ParseNumber: Invalid number format");
                }
                charString.append(currentChar);
                hasDecimal = true;
            } else {
                break;
            }
            getNextChar();
        }
        Integer value = Integer.valueOf(charString.toString());
        return new Token(TokenType.DIGIT, value);
    }

    private Token parseString() {
        StringBuilder charString = new StringBuilder();
        getNextChar();

        while (position < json.length()) {
            char currentChar = getCurrentChar();
            if (currentChar == '"') {
                // End of string
                getNextChar();
                return new Token(TokenType.STRING, charString.toString());
            } else if (currentChar == '\\') {
                //Handle escape sequences
                getNextChar();
                char escapedChar = getCurrentChar();

                switch (escapedChar) {
                    case '"':
                    case '\\':
                    case '/':
                        charString.append(escapedChar);
                        break;
                    case 'b':
                        charString.append('\b');
                        break;
                    case 'f':
                        charString.append('\f');
                        break;
                    case 'n':
                        charString.append('\n');
                        break;
                    case 'r':
                        charString.append('\r');
                        break;
                    case 't':
                        charString.append('\t');
                        break;
                    case 'u':
                        //implement unicode escape sequence
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid escape sequence in string");
                }
            } else {
                charString.append(currentChar);
            }
            getNextChar();
        }
        throw new IllegalArgumentException("Invalid JSON: unterminated String");
    }

    public List<Token> doParse() {
        List<Token> tokenList = new ArrayList<>();
        while (position < json.length()) {
            Token token = parse();

            if (token.type().equals(TokenType.STRING) || token.type().equals(TokenType.BOOLEAN)
                    || token.type().equals(TokenType.DIGIT) || token.type().equals(TokenType.NULL)) {
                tokenList.add(token);
            } else {
                tokenList.add(token);
                getNextChar();
            }
        }
        return tokenList;
    }
}
