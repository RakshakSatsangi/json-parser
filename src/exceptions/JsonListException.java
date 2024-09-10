package exceptions;

public class JsonListException extends JsonException {
    public JsonListException(String message) {
        super(message);
    }

    public JsonListException(Throwable cause) {
        super(cause);
    }
}
