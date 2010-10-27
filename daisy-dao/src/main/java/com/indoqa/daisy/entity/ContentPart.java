package com.indoqa.daisy.entity;

import java.io.Serializable;

public class ContentPart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String text;

    public String getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
}
