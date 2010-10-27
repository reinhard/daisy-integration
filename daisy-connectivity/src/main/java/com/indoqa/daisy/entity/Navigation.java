package com.indoqa.daisy.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cocoon.sitemap.util.WildcardMatcherHelper;

public class Navigation extends DaisyDomainObject {

    private static final long serialVersionUID = 1L;

    private Map<String, NavigationElement> elByDocId = new HashMap<String, NavigationElement>();
    private Map<String, NavigationElement> elByPath = new HashMap<String, NavigationElement>();
    private Map<String, String> linkTranslationMap = new HashMap<String, String>();
    private NavigationElement root;

    public Map<String, String> getLinkTranslationMap() {
        return this.linkTranslationMap;
    }

    public NavigationElement getNavigationElementByDocumentId(String documentId) {
        return this.elByDocId.get(documentId);
    }

    public NavigationElement getNavigationElementByPath(String path) {
        return this.elByPath.get(path);
    }

    public NavigationElement[] getNavigationElementsByPath(String wildcardPath) {
        List<NavigationElement> navigationElements = new ArrayList<NavigationElement>();

        Set<String> paths = this.elByPath.keySet();
        for (String currentPath : paths) {
            Map<String, String> result = WildcardMatcherHelper.match(wildcardPath, currentPath);
            if (result != null) {
                navigationElements.add(this.elByPath.get(currentPath));
            }
        }

        return navigationElements.toArray(new NavigationElement[navigationElements.size()]);
    }

    public NavigationElement getRoot() {
        return this.root;
    }

    public Navigation setRoot(NavigationElement root) {
        this.root = root;

        this.indexByPath(root);
        this.indexByDocumentId(root);
        this.createTranslationMap(root);

        return this;
    }

    private void createTranslationMap(NavigationElement element) {
        this.linkTranslationMap.put(element.getOriginalPath(), element.getPath());

        for (NavigationElement curNavigationElement : element.getChildren()) {
            this.createTranslationMap(curNavigationElement);
        }
    }

    private void indexByDocumentId(NavigationElement element) {
        this.elByDocId.put(element.getDocumentId(), element);

        for (NavigationElement curNavigationElement : element.getChildren()) {
            this.indexByDocumentId(curNavigationElement);
        }
    }

    private void indexByPath(NavigationElement element) {
        this.elByPath.put(element.getPath(), element);

        for (NavigationElement curNavigationElement : element.getChildren()) {
            this.indexByPath(curNavigationElement);
        }
    }
}
