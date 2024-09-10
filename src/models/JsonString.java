package models;

public class JsonString implements JsonElement {
    private final String value;

    public JsonString(String value) {
        this.value = value;
    }

    @Override
    public String getVal() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
