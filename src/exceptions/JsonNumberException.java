package exceptions;

public class JsonNumberException extends JsonException {
    public JsonNumberException(String message) {
        super(message);
    }

    public JsonNumberException(Throwable cause) {
        super(cause);
    }
}
