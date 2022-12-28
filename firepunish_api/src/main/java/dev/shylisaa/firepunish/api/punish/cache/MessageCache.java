package dev.shylisaa.firepunish.api.punish.cache;

import com.google.gson.JsonElement;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MessageCache {

    private final ConcurrentHashMap<String, JsonElement> messageCache;

    public MessageCache() {
        this.messageCache = new ConcurrentHashMap<>();
    }

    public void cacheMessage(String identifier, JsonElement message) {
        this.messageCache.put(identifier, message);
    }

    public JsonElement getFromMessageCache(String identifier) {
        return this.messageCache.get(identifier);
    }

    public Integer getCacheValueCount() {
        return messageCache.size();
    }
}
