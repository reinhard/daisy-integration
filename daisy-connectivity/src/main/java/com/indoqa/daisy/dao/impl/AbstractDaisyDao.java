package com.indoqa.daisy.dao.impl;

import org.apache.cocoon.configuration.Settings;
import org.springframework.beans.factory.annotation.Autowired;

import com.indoqa.daisy.adapter.DaisyRepositoryAccessFacade;

public abstract class AbstractDaisyDao {

    @Autowired
    private DaisyRepositoryAccessFacade daisyRepositoryAccessFacade;

    @Autowired
    private Settings settings;

    protected String getDaisyNamespace() {
        return this.settings.getProperty("com.indoqa.daisy.namespace");
    }

    protected DaisyRepositoryAccessFacade getDaisyRepositoryAccessFacade() {
        return this.daisyRepositoryAccessFacade;
    }

    protected String getNavDocId() {
        return this.settings.getProperty("com.indoqa.daisy.service.content.navdoc");
    }

    protected Settings getSettings() {
        return this.settings;
    }
}
