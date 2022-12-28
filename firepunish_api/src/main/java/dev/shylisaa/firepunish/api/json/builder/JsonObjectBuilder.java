package dev.shylisaa.firepunish.api.json.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonObjectBuilder {

    private final JsonObject object;

    private JsonObjectBuilder() {
        this.object = new JsonObject();
    }

    public static JsonObjectBuilder builder() {
        return new JsonObjectBuilder();
    }

    public JsonObjectBuilder put(String key, String value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder put(String key, Number value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder put(String key, Boolean value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder put(String key, JsonElement value) {
        this.object.add(key, value);
        return this;
    }

    public JsonObjectBuilder put(String key, Character value) {
        this.object.addProperty(key, value);
        return this;
    }

    public JsonObject build() {
        return this.object;
    }
}
