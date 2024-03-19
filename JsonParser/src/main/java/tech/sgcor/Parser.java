package tech.sgcor;

import tech.sgcor.exception.JsonNotValidException;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final String json;
    private List<Token> tokens;
    private int currentTokenIndex;

    public Parser(String json) {
        this.json = json;
        this.currentTokenIndex = 0;
    }

    private void validateJson(String json) {
        String jsonString = json.trim();
        char firstChar = jsonString.charAt(0);
        char lastCharInJson = jsonString.charAt(jsonString.length() - 1);

        if (jsonString.isBlank() || firstChar != '{' || lastCharInJson != '}') {
            throw new JsonNotValidException("Invalid Json: must start with '{' and end with '}'");
        }

        int depth = 0;
        boolean inString = false;
        char lastChar = 0;

        for (char c : jsonString.toCharArray()) {
            switch (c) {
                case '{', '[' -> {
                    if (!inString) {
                        depth++;
                    }
                }
                case '}', ']' -> {
                    if (!inString) {
                        depth--;
                        if (depth < 0) {
                            throw new JsonNotValidException("Invalid JSON: Unbalanced braces or brackets");
                        }
                    }
                }
                case '"' -> {
                    if (lastChar != '\\' && !inString) {
                        inString = true;
                    } else if (lastChar != '\\') {
                        inString = false;
                    }
                }
                case ',' -> {
                    if (!inString && (lastChar == ',' || lastChar == '{' || lastChar == '[' || lastChar == ':')) {
                        throw new JsonNotValidException("Invalid JSON: Misplaced comma ','");
                    }
                }
            }
            lastChar = c;
        }
        if (depth != 0) {
            throw new JsonNotValidException("Invalid JSON: Unbalanced braces or brackets");
        }
    }


    public JsonObject parse() {
        try {
            validateJson(json);
            Lexer lexer = new Lexer(json);
            tokens = lexer.doParse();
        } catch (JsonNotValidException | IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        return parseObject();
    }

    private JsonObject parseObject() {
        JsonObject jsonObject = new JsonObject();

        consume(TokenType.LEFT_BRACE);
        
        while (peek().type() != TokenType.RIGHT_BRACE) {
            Token tokenKey = consume(TokenType.STRING);
            String key = String.valueOf(tokenKey.value());
            consume(TokenType.COLON);
            
            Token tokenValue = peek();
            if (tokenValue.type() == TokenType.STRING) {
                jsonObject.addProperties(key, tokenValue.value());
                consume(TokenType.STRING);
            } else if (tokenValue.type() == TokenType.BOOLEAN ||
                    tokenValue.type() == TokenType.DIGIT || tokenValue.type() == TokenType.NULL) {
                jsonObject.addProperties(key, tokenValue.value());
                currentTokenIndex++;
            } else if (tokenValue.type() == TokenType.LEFT_BRACE) {
                JsonObject nestedObject = parseObject();
                jsonObject.addProperties(key, nestedObject);
            } else if (tokenValue.type() == TokenType.LEFT_BRACKET) {
                List<Object> nestedArray = parseArray();
                jsonObject.addProperties(key, nestedArray);
            } else {
                System.err.println("Unexpected token type: " + tokenValue);
                System.exit(1);
            }

            if (peek().type() == TokenType.COMMA) {
                consume(TokenType.COMMA);
            }
        }

        consume(TokenType.RIGHT_BRACE);

        return jsonObject;
    }

    private List<Object> parseArray() {
        List<Object> jsonArray = new ArrayList<>();
        
        consume(TokenType.LEFT_BRACKET);
        
        while (peek().type() != TokenType.RIGHT_BRACKET) {
            Token tokenValue = peek();
            
            if (tokenValue.type() == TokenType.STRING) {
                jsonArray.add(tokenValue.value());
                consume(TokenType.STRING);
            } else if (tokenValue.type() == TokenType.BOOLEAN ||
                    tokenValue.type() == TokenType.DIGIT || tokenValue.type() == TokenType.NULL) {
                jsonArray.add(tokenValue.value());
                consume(tokenValue.type());
            } else if (tokenValue.type() == TokenType.LEFT_BRACKET) {
                List<Object> nestedArray = parseArray();
                jsonArray.add(nestedArray);
            } else {
                System.err.println("Unexpected token type: " + tokenValue.type());
            }

            if (peek().type() == TokenType.COMMA) {
                consume(TokenType.COMMA);
            }
        }

        consume(TokenType.RIGHT_BRACKET);
        return jsonArray;
    }

    private Token consume(TokenType expectedType) {
        Token token = peek();
        
        if (token.type() != expectedType) {
            System.err.println("Invalid JSON Provided: " + expectedType + " expected not " + token.type());
            System.exit(1);
        }

        currentTokenIndex++;

        return token;
    }

    private Token peek() {

        if (currentTokenIndex >= tokens.size()) {
            System.err.println("No more token to peek");
            System.exit(1);
        }
        return tokens.get(currentTokenIndex);
    }
}
