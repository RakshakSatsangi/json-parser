package exceptions;

public class JsonStringException extends JsonException {
    public JsonStringException(String message) {
        super(message);
    }

    public JsonStringException(Throwable cause) {
        super(cause);
    }
}
