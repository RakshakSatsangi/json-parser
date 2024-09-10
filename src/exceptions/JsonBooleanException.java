package exceptions;

public class JsonBooleanException extends JsonException {
    public JsonBooleanException(String message) {
        super(message);
    }

    public JsonBooleanException(Throwable cause) {
        super(cause);
    }
}
