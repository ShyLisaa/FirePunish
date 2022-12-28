package dev.shylisaa.firepunish.api.json;

import com.google.gson.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFile {
    
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    
    private JsonObject jsonObject;

    public JsonFile() {
        this(new JsonObject());
    }

    private JsonFile(final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonFile(final Path path) {
        this.read(path);
    }

    public <T> T get(final String key, final Class<T> clazz) {
        return GSON.fromJson(this.jsonObject.get(key), clazz);
    }

    public <T> T get(final String key, final Type type) {
        return GSON.fromJson(this.jsonObject.get(key), type);
    }

    public JsonFile addIfNotExists(String key, boolean state) {
        if(has(key)) set(key, state);
        return this;
    }

    public JsonFile addIfNotExists(String key, int state) {
        if(has(key)) set(key, state);
        return this;
    }

    public JsonFile addIfNotExists(String key, String state) {
        if(has(key)) set(key, state);
        return this;
    }

    public JsonFile addIfNotExists(String key, JsonArray array) {
        if(has(key)) set(key, array);
        return this;
    }

    public <T> T get(final Class<T> clazz) {
        return GSON.fromJson(this.jsonObject, clazz);
    }

    public <T> T get(final Type type) {
        return GSON.fromJson(this.jsonObject, type);
    }

    public JsonFile set(final String key, final Object object) {
        this.jsonObject.add(key, GSON.toJsonTree(object, object.getClass()));
        return this;
    }

    public void read(final Path path) {
        if(!Files.exists(path)) return;
        try (final var fileReader = Files.newBufferedReader(path)) {
            this.jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean has(String key) {
        return !this.jsonObject.has(key);
    }

    /**
     * Will Forece-Write to the File without checking if File exists.
     * @Deprecated Use JsonFile#writeToFile(Path) instead.
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public void forceWrite(final Path path) {
        try (final var writer = Files.newBufferedWriter(path)) {
            writer.write(GSON.toJson(this.jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonFile writeToFile(final Path path) {
        try (final var writer = Files.newBufferedWriter(path)) {
            writer.write(GSON.toJson(this.jsonObject));
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    @Override
    public String toString() {
        return this.jsonObject.toString();
    }
}
