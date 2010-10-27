package com.indoqa.daisy.entity;

import java.io.Serializable;

public class Part implements Serializable {

    private static final long serialVersionUID = 1l;
    private String type;

    public Part(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[type=" + this.type + "]";
    }
}