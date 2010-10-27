package com.indoqa.daisy.service;

import java.util.List;
import java.util.Locale;

import com.indoqa.daisy.entity.BinaryDocument;
import com.indoqa.daisy.entity.ContentDocument;
import com.indoqa.daisy.entity.NavigationElement;

public interface ContentService {

    List<ContentDocument> find(String query, Locale locale);

    BinaryDocument getBinaryDocument(String id, String partType, String fileName, Locale locale);

    ContentDocument getContentDocumentById(String id, String relativizer, Locale locale);

    ContentDocument getContentDocumentByPath(String path, Locale locale);

    NavigationElement[] getMenuNavigationElementsForPath(String path, Locale locale);
}
