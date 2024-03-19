package tech.sgcor.exception;

public class JsonNotValidException extends RuntimeException{
    public JsonNotValidException(String message) {
        super(message);
    }
}
