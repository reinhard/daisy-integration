package com.indoqa.daisy.dao;

import java.util.Locale;

import com.indoqa.daisy.entity.Navigation;

public interface NavigationDao {

    Navigation get(String id, Locale locale);

    String getNavDocId();
}
