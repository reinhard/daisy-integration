package com.indoqa.daisy.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContentField implements Serializable {

    private static final long serialVersionUID = 1l;

    private String name;
    private List<String> values = new ArrayList<String>();
    private String valueType;
    private List<String> documentIds = new ArrayList<String>();
    private List<String> languageIds = new ArrayList<String>();
    private List<String> brancheIds = new ArrayList<String>();
    private List<ContentDocument> documents = new ArrayList<ContentDocument>();

    public ContentField() {
        this(null, null);
    }

    public ContentField(String name, String valueType) {
        this.name = name;
        this.valueType = valueType;
    }

    public void addBranchId(String branchId) {
        this.brancheIds.add(branchId);
    }

    public void addDocument(ContentDocument document) {
        this.documents.add(document);
    }

    public void addDocumentId(String documentId) {
        this.documentIds.add(documentId);
    }

    public void addLanguageId(String languageId) {
        this.languageIds.add(languageId);
    }

    public void addValue(String value) {
        this.values.add(value);
    }

    public List<String> getBrancheIds() {
        return this.brancheIds;
    }

    public List<String> getDocumentIds() {
        return this.documentIds;
    }

    public List<ContentDocument> getDocuments() {
        return this.documents;
    }

    public List<String> getLanguageIds() {
        return this.languageIds;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        String[] result = this.getValues();

        if (result != null && result.length >= 1) {
            return result[0];
        }

        return null;
    }

    public String[] getValues() {
        return this.values.toArray(new String[this.values.size()]);
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[name=" + this.name + ", value=" + this.values + "]";
    }
}
