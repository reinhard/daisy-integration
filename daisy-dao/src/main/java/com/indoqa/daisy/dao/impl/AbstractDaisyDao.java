package com.indoqa.daisy.dao.impl;

import org.apache.cocoon.configuration.Settings;
import org.springframework.beans.factory.annotation.Autowired;

import com.indoqa.daisy.adapter.DaisyRepositoryAccessFacade;

public abstract class AbstractDaisyDao {

    @Autowired
    private DaisyRepositoryAccessFacade daisyRepositoryAccessFacade;

    @Autowired
    private Settings settings;

    protected DaisyRepositoryAccessFacade getDaisyRepositoryAccessFacade() {
        return this.daisyRepositoryAccessFacade;
    }

    protected Settings getSettings() {
        return this.settings;
    }
}
