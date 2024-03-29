package com.indoqa.daisy.dao;

import java.util.Locale;

import com.indoqa.daisy.entity.BinaryDocument;

public interface BinaryDocumentDao {

    BinaryDocument get(String id, Locale locale, String part, String fileName);

    String getFileName(String id, String partName, Locale locale);
}
