package com.indoqa.daisy.dao;

import java.util.Locale;

import com.indoqa.daisy.entity.Navigation;

public interface NavigationDao {

    Navigation get(long id, Locale locale);
}
