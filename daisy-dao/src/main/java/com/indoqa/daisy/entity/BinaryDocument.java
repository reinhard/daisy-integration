package com.indoqa.daisy.entity;

public class BinaryDocument extends DaisyDomainObject {

    private static final long serialVersionUID = 1L;

    private byte[] content;
    private String mimeType;
    private long lastModified;

    public byte[] getContent() {
        return this.content;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
