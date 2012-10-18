package com.indoqa.daisy.caching;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class InMemoryCache implements Cache {

    private static final Map<Serializable, Result> CACHE = new ConcurrentHashMap<Serializable, Result>(500, 0.9f, 4);

    @Override
    public void clear() {
        CACHE.clear();
    }

    @Override
    public Result get(Serializable key) {
        return CACHE.get(key);
    }

    @Override
    public void put(Serializable key, Result value) {
        CACHE.put(key, value);
    }
}
