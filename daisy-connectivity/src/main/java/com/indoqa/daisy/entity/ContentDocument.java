package com.indoqa.daisy.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContentDocument extends DaisyDomainObject {

    private static final long serialVersionUID = 1L;
    private static final ContentPart EMPTY_PART = new ContentPart();
    private static final ContentField EMPTY_FIELD = new ContentField();

    private Map<String, ContentPart> contentParts = new HashMap<String, ContentPart>();
    private Map<String, ContentField> fields;
    private String name;
    private String summary;
    private String type;
    private String path;
    private String author;
    private String lastModified;
    private String created;

    public void addContentPart(ContentPart contentPart) {
        this.contentParts.put(contentPart.getType(), contentPart);
    }

    public String getAuthor() {
        return this.author;
    }

    public ContentPart getContentPart(String partType) {
        ContentPart contentPart = this.contentParts.get(partType);

        if (contentPart == null) {
            return EMPTY_PART;
        }

        return contentPart;
    }

    public Collection<ContentPart> getContentParts() {
        return this.contentParts.values();
    }

    public String getCreated() {
        return this.created;
    }

    public ContentField getField(String fieldName) {
        ContentField contentField = this.fields.get(fieldName);

        if (contentField == null) {
            return EMPTY_FIELD;
        }

        return contentField;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getType() {
        return this.type;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setFields(Map<String, ContentField> fields) {
        this.fields = fields;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setType(String type) {
        this.type = type;
    }
}
