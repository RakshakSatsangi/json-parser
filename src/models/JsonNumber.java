package models;

public class JsonNumber implements JsonElement {
    private final Number value;

    public JsonNumber(Number value) {
        this.value = value;
    }

    @Override
    public Number getVal() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
