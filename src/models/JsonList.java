package models;

import java.util.List;

public class JsonList implements JsonElement {
    private final List<JsonElement> elements;

    public JsonList(List<JsonElement> elements) {
        this.elements = elements;
    }

    @Override
    public List<JsonElement> getVal() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
