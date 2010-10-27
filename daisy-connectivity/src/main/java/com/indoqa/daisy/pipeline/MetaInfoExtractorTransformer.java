package com.indoqa.daisy.pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cocoon.sax.AbstractSAXTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.indoqa.daisy.entity.ContentField;
import com.indoqa.daisy.entity.Part;

/**
 * An extractor to read specific information from a Daisy document (parts, fields).
 */
public class MetaInfoExtractorTransformer extends AbstractSAXTransformer {

    private static final String EL_DOC = "document";
    private static final String EL_FIELD = "field";
    private static final String EL_PART = "part";
    private static final String EL_SUMMARY = "summary";
    private static final String NS_DAISY = "http://outerx.org/daisy/1.0";

    private ContentField currentField;
    private Map<String, ContentField> fields = new HashMap<String, ContentField>();
    private boolean inField;
    private boolean inSummary;
    private String name;
    private List<Part> parts = new ArrayList<Part>();
    private StringBuilder summary = new StringBuilder();
    private String type;
    private String lastModified;
    private String author;
    private String created;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.inSummary) {
            this.summary.append(ch, start, length);
        }

        super.characters(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (NS_DAISY.equals(uri)) {
            if (EL_FIELD.equals(localName)) {
                this.inField = false;
            }

            if (EL_SUMMARY.equals(localName)) {
                this.inSummary = false;
            }
        }

        super.endElement(uri, localName, name);
    }

    public String getAuthor() {
        return this.author;
    }

    public String getCreated() {
        return this.created;
    }

    public Map<String, ContentField> getFields() {
        return this.fields;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public String getName() {
        return this.name;
    }

    public List<Part> getParts() {
        return this.parts;
    }

    public String getSummary() {
        return this.summary.toString();
    }

    public String getType() {
        return this.type;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        if (this.inField) {
            if ("link".equals(localName)) {
                this.currentField.addDocumentId(atts.getValue("documentId"));
                this.currentField.addBranchId(atts.getValue("languageId"));
                this.currentField.addLanguageId(atts.getValue("branchId"));
            }

            String value = atts.getValue("valueFormatted");
            this.currentField.addValue(value);
        }

        if (NS_DAISY.equals(uri)) {
            if (EL_FIELD.equals(localName)) {
                this.inField = true;
                this.currentField = new ContentField(atts.getValue("name"), atts.getValue("valueType"));
                this.fields.put(this.currentField.getName(), this.currentField);
            }

            if (EL_PART.equals(localName)) {
                Part part = new Part(atts.getValue("name"));
                this.parts.add(part);
            }

            if (EL_DOC.equals(localName)) {
                this.type = atts.getValue("typeName");
                this.name = atts.getValue("name");
                this.author = atts.getValue("ownerDisplayName");
                this.lastModified = atts.getValue("lastModified");
                this.created = atts.getValue("created");
            }

            if (EL_SUMMARY.equals(localName)) {
                this.inSummary = true;
            }
        }

        super.startElement(uri, localName, name, atts);
    }
}
