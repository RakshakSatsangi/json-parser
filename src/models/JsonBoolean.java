package models;

public class JsonBoolean implements JsonElement {
    private final Boolean value;

    public JsonBoolean(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getVal() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
