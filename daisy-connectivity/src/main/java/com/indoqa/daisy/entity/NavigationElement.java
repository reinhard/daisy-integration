package com.indoqa.daisy.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

public class NavigationElement implements Serializable {

    private static final long serialVersionUID = 1l;

    private List<NavigationElement> children = new ArrayList<NavigationElement>();
    private String documentId;
    private String id;
    private String label;
    private String originalPath;
    private NavigationElement parent;
    private boolean root;

    private String path;

    private boolean separator;

    public NavigationElement(NavigationElement parent) {
        Validate.notNull(parent, "A parent navigation element has to be passed. Use the 'createRoot'"
                + " method to create a root navigation element.");

        this.parent = parent;
        parent.addChild(this);
    }

    private NavigationElement() {
        // no external instantiation without a parent element
    }

    public static NavigationElement createRoot() {
        NavigationElement navigationElement = new NavigationElement();

        navigationElement.setPath("");
        navigationElement.setLabel("Root");
        navigationElement.setRoot(true);

        return navigationElement;
    }

    public void addChild(NavigationElement navigationElement) {
        this.children.add(navigationElement);
    }

    public List<NavigationElement> getChildren() {
        return this.children;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getOriginalPath() {
        return this.originalPath;
    }

    public NavigationElement getParent() {
        return this.parent;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isRoot() {
        return this.root;
    }

    public boolean isSeparator() {
        return this.separator;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }

    @Override
    public String toString() {
        return "[" + this.getDocumentId() + "] path=" + this.getPath();
    }
}
