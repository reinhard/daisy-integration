package com.indoqa.daisy.pipeline;

import java.util.Map;

import org.apache.cocoon.configuration.Settings;
import org.apache.cocoon.sax.AbstractSAXGenerator;

import com.indoqa.daisy.adapter.DaisyDocument;
import com.indoqa.daisy.adapter.DaisyDocumentProxy;
import com.indoqa.daisy.adapter.DaisyRepositoryAccessFacade;
import com.indoqa.daisy.adapter.pr.PRDocumentConfiguration;

/**
 * @version $Id: AbstractDaisyProducer.java 2516 2010-04-01 20:20:24Z rpoetz $
 */
public abstract class AbstractDaisyProducer extends AbstractSAXGenerator {

    protected Settings cocoonSettings;

    protected String documentId;

    protected String part;

    private String annotationNavDocId;

    private DaisyDocument daisyDocument;

    private DaisyRepositoryAccessFacade facade;

    private boolean isNavDoc;

    public void setAnnotationNavDocId(String annotationNavDocId) {
        this.annotationNavDocId = annotationNavDocId;
    }

    public void setCocoonSettings(Settings cSettings) {
        this.cocoonSettings = cSettings;
    }

    @Override
    public void setConfiguration(Map<String, ? extends Object> configuration) {
        super.setConfiguration(configuration);

        this.setDocumentId((String) configuration.get("documentId"));
        this.setIsNavDoc(Boolean.parseBoolean((String) configuration.get("isNavDoc")));
        this.part = (String) configuration.get("part");
    }

    public void setDaisyAccessFacade(DaisyRepositoryAccessFacade facade) {
        this.facade = facade;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setIsNavDoc(boolean isNavDoc) {
        this.isNavDoc = isNavDoc;
    }

    public void setPart(String part) {
        this.part = part;
    }

    protected DaisyDocument getDaisyDocument() {
        if (this.daisyDocument == null) {
            PRDocumentConfiguration config = new PRDocumentConfiguration();
            config.annotatingNavDocId = this.annotationNavDocId;
            config.isNavigationDocument = this.isNavDoc;
            if (SettingsHelper.useLast(this.cocoonSettings)) {
                config.useLast = true;
            } else {
                config.useLast = false;
            }

            this.daisyDocument = new DaisyDocumentProxy(this.documentId, this.facade, config, "1", "1");
            if (this.daisyDocument.getDocument().getLiveVersionId() < 0 && !config.useLast) {
                throw new DaisyDocumentNotFoundException("Can't find document with ID " + this.documentId);
            }

            if (this.part != null) {
                try {
                    this.daisyDocument.getDocument().getPart(this.part);
                } catch (Exception e) {
                    throw new DaisyDocumentNotFoundException("Can't find part with name " + this.part + " of document with ID "
                            + this.documentId);
                }
            }
        }

        return this.daisyDocument;
    }
}
