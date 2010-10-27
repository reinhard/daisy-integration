package com.indoqa.daisy.caching;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Object value;
    private final long validUntil;

    public Result(Object value, long validUntil) {
        this.value = value;
        this.validUntil = validUntil;
    }

    public long getValidUntil() {
        return this.validUntil;
    }

    public Object getValue() {
        return this.value;
    }
}
