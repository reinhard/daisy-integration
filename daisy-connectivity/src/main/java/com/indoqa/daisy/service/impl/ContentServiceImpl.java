package com.indoqa.daisy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indoqa.daisy.dao.BinaryDocumentDao;
import com.indoqa.daisy.dao.ContentDocumentDao;
import com.indoqa.daisy.dao.NavigationDao;
import com.indoqa.daisy.entity.BinaryDocument;
import com.indoqa.daisy.entity.ContentDocument;
import com.indoqa.daisy.entity.Navigation;
import com.indoqa.daisy.entity.NavigationElement;
import com.indoqa.daisy.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDocumentDao contentDocumentDao;

    @Autowired
    private NavigationDao navigationDao;

    @Autowired
    private BinaryDocumentDao binaryDocumentDao;

    private static String createRelativizer(String path) {
        if (StringUtils.isBlank(path)) {
            return "";
        }

        String result = path;
        if (!result.startsWith("/")) {
            result = "/" + result;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.countMatches(path, "/"); i++) {
            sb.append("../");
        }

        return sb.toString();
    }

    @Override
    public List<ContentDocument> find(String query, Locale locale) {
        List<String> docIds = this.contentDocumentDao.find(query, locale);

        // check if the result documents exist
        List<String> existingDocIds = new ArrayList<String>();
        Navigation navigation = this.getNavigation(locale);
        for (String docId : docIds) {
            NavigationElement navigationElement = navigation.getNavigationElementByDocumentId(docId);

            if (navigationElement != null && StringUtils.isNotEmpty(navigationElement.getDocumentId())) {
                existingDocIds.add(docId);
            }
        }

        // load the document
        List<ContentDocument> results = new ArrayList<ContentDocument>();
        for (String documentId : existingDocIds) {
            results.add(this.getContentDocument(documentId, locale, "", null));
        }

        return results;
    }

    @Override
    public BinaryDocument getBinaryDocument(String id, String partType, String fileName, Locale locale) {
        return this.binaryDocumentDao.get(id, locale, partType, fileName);
    }

    @Override
    public ContentDocument getContentDocumentById(String id, String relativizer, Locale locale) {
        Navigation navigation = this.getNavigation(locale);

        // check if the document exists
        NavigationElement navigationElement = navigation.getNavigationElementByDocumentId(id);
        if (navigationElement == null || StringUtils.isEmpty(navigationElement.getDocumentId())) {
            return null;
        }

        Map<String, String> linkTranslationMap = navigation.getLinkTranslationMap();
        return this.getContentDocument(id, locale, relativizer, linkTranslationMap);
    }

    @Override
    public ContentDocument getContentDocumentByPath(String path, Locale locale) {
        Navigation navigation = this.getNavigation(locale);

        NavigationElement navigationElement = navigation.getNavigationElementByPath(path);
        if (navigationElement == null || StringUtils.isEmpty(navigationElement.getDocumentId())) {
            return null;
        }

        Map<String, String> linkTranslationMap = navigation.getLinkTranslationMap();
        return this.getContentDocument(navigationElement.getDocumentId(), locale, createRelativizer(path), linkTranslationMap);
    }

    @Override
    public NavigationElement[] getMenuNavigationElementsForPath(String path, Locale locale) {
        NavigationElement currentElement = this.getNavigation(locale).getNavigationElementByPath(path);

        if (currentElement == null) {
            return new NavigationElement[0];
        }

        // navigate to the element one above the root element
        while (currentElement.getParent() != null && !currentElement.getParent().isRoot()) {
            currentElement = currentElement.getParent();
        }

        // add the navigation element at the first level and its children
        List<NavigationElement> elements = new ArrayList<NavigationElement>();
        if (StringUtils.isNotBlank(currentElement.getDocumentId())) {
            elements.add(currentElement);
        }
        for (NavigationElement currentChildElement : currentElement.getChildren()) {
            if (StringUtils.isNotBlank(currentChildElement.getDocumentId())) {
                elements.add(currentChildElement);
            }
        }

        return elements.toArray(new NavigationElement[elements.size()]);
    }

    private ContentDocument getContentDocument(String documentId, Locale locale, String pathRelativizer,
            Map<String, String> linkRewriteTranslationTable) {
        ContentDocument contentDocument = this.contentDocumentDao
                .get(documentId, locale, pathRelativizer, linkRewriteTranslationTable);

        // add the navigation path to the content document
        NavigationElement navEl = this.getNavigation(locale).getNavigationElementByDocumentId(documentId);
        if (navEl != null) {
            contentDocument.setPath(navEl.getPath());
        }

        return contentDocument;
    }

    private Navigation getNavigation(Locale locale) {
        return this.navigationDao.get(this.navigationDao.getNavDocId(), locale);
    }
}
