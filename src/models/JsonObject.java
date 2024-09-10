package models;

import java.util.Map;

public class JsonObject implements JsonElement {
    private final Map<String, JsonElement> elementsMap;

    public JsonObject(Map<String, JsonElement> elementsMap) {
        this.elementsMap = elementsMap;
    }

    @Override
    public Map<String, JsonElement> getVal() {
        return elementsMap;
    }

    @Override
    public String toString() {
        return elementsMap.toString();
    }
}
