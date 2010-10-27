package com.indoqa.daisy.entity;

import java.io.Serializable;
import java.util.Locale;

public abstract class DaisyDomainObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Locale locale;

    private String path;

    public String getId() {
        return this.id;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getPath() {
        return this.path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
