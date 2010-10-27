package com.indoqa.daisy.caching;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class InMemoryCache implements Cache {

    private static final Map<Serializable, Result> CACHE = new HashMap<Serializable, Result>();

    @Override
    public Result get(Serializable key) {
        return CACHE.get(key);
    }

    @Override
    public void put(Serializable key, Result value) {
        CACHE.put(key, value);
    }
}
