package com.indoqa.daisy.dao;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.indoqa.daisy.entity.ContentDocument;

public interface ContentDocumentDao {

    List<String> find(String query, Locale locale);

    ContentDocument get(String id, Locale locale, String pathRelativizer, Map<String, String> linkRewriteTranslationTable);
}
