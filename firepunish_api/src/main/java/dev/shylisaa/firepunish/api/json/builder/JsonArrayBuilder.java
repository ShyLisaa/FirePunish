package dev.shylisaa.firepunish.api.json.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonArrayBuilder {

    private final JsonArray jsonArray;

    private JsonArrayBuilder() {
        this.jsonArray = new JsonArray();
    }

    public static JsonArrayBuilder builder() {
        return new JsonArrayBuilder();
    }

    public JsonArrayBuilder put(String key) {
        this.jsonArray.add(key);
        return this;
    }

    public JsonArrayBuilder put(Integer key) {
        this.jsonArray.add(key);
        return this;
    }

    public JsonArrayBuilder put(JsonObject object) {
        this.jsonArray.add(object);
        return this;
    }

    public JsonArrayBuilder put(Boolean key) {
        this.jsonArray.add(key);
        return this;
    }

    public JsonArrayBuilder put(Number key) {
        this.jsonArray.add(key);
        return this;
    }

    public JsonArrayBuilder put(Character key) {
        this.jsonArray.add(key);
        return this;
    }

    public JsonArrayBuilder put(JsonArray array) {
        this.jsonArray.add(array);
        return this;
    }

    public JsonArray build() {
        return this.jsonArray;
    }
}
