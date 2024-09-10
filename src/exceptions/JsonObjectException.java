package exceptions;

public class JsonObjectException extends JsonException {
    public JsonObjectException(String message) {
        super(message);
    }

    public JsonObjectException(Throwable cause) {
        super(cause);
    }
}
