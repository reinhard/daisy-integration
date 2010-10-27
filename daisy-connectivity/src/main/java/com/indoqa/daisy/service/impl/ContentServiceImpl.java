package com.indoqa.daisy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
        List<Long> docIds = this.contentDocumentDao.find(query, locale);

        List<ContentDocument> results = new ArrayList<ContentDocument>();
        for (Long documentId : docIds) {
            results.add(this.getContentDocument(documentId, locale, "", null));
        }

        return results;
    }

    @Override
    public BinaryDocument getBinaryDocument(String id, String partType, String fileName, Locale locale) {
        long documentId;
        try {
            documentId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            return null;
        }

        return this.binaryDocumentDao.get(documentId, locale, partType, fileName);
    }

    @Override
    public ContentDocument getContentDocumentById(String id, String relativizer, Locale locale) {
        Navigation navigation = this.getNavigation(locale);

        // check if the document exists
        NavigationElement navigationElement = navigation.getNavigationElementByDocumentId(id);
        if (navigationElement == null || !NumberUtils.isDigits(navigationElement.getDocumentId())) {
            return null;
        }

        long documentId;
        try {
            documentId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            return null;
        }

        Map<String, String> linkTranslationMap = navigation.getLinkTranslationMap();
        return this.getContentDocument(documentId, locale, relativizer, linkTranslationMap);
    }

    @Override
    public ContentDocument getContentDocumentByPath(String path, Locale locale) {
        Navigation navigation = this.getNavigation(locale);

        NavigationElement navigationElement = navigation.getNavigationElementByPath(path);
        if (navigationElement == null || !NumberUtils.isDigits(navigationElement.getDocumentId())) {
            return null;
        }

        long documentId = Long.parseLong(navigationElement.getDocumentId());
        Map<String, String> linkTranslationMap = navigation.getLinkTranslationMap();
        return this.getContentDocument(documentId, locale, createRelativizer(path), linkTranslationMap);
    }

    @Override
    public NavigationElement[] getMenuNavigationElementsForPath(String path, Locale locale) {
        NavigationElement currentElement = this.getNavigation(locale).getNavigationElementByPath(path);

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

    private ContentDocument getContentDocument(long documentId, Locale locale, String pathRelativizer,
            Map<String, String> linkRewriteTranslationTable) {
        ContentDocument contentDocument = this.contentDocumentDao
                .get(documentId, locale, pathRelativizer, linkRewriteTranslationTable);

        // add the navigation path to the content document
        NavigationElement navEl = this.getNavigation(locale).getNavigationElementByDocumentId(Long.toString(documentId));
        if (navEl != null) {
            contentDocument.setPath(navEl.getPath());
        }

        return contentDocument;
    }

    private Navigation getNavigation(Locale locale) {
        // TODO replace document id with configureable value
        return this.navigationDao.get(755, locale);
    }
}
