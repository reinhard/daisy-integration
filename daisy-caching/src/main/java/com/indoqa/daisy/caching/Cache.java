package com.indoqa.daisy.caching;

import java.io.Serializable;

public interface Cache {

    void clear();

    Result get(Serializable key);

    void put(Serializable key, Result value);
}
