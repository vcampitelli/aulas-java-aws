package com.viniciuscampitelli.mock;

import java.util.Date;
import java.util.HashMap;

public class Jedis {
    private static final HashMap<String, CacheEntry> cache = new HashMap<String, CacheEntry>();

    public Boolean exists(String cacheKey) {
        return cache.containsKey(cacheKey);
    }

    public String get(String cacheKey) {
        CacheEntry entry = cache.get(cacheKey);
        if (entry == null) {
            return null;
        }

        if (entry.expiration().isBefore((new Date()).toInstant())) {
            return null;
        }

        return entry.value();
    }

    public Jedis setex(String cacheKey, Integer seconds, String value) {
        cache.put(
                cacheKey,
                new CacheEntry(value, (new Date()).toInstant().plusSeconds(seconds))
        );
        return this;
    }
}
