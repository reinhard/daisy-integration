package com.indoqa.daisy.wicket;

import org.apache.wicket.Session;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.indoqa.daisy.entity.ContentDocument;
import com.indoqa.daisy.service.ContentService;

public class ContentDocumentModel extends LoadableDetachableModel<ContentDocument> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ContentService contentService;

    private String path;

    public ContentDocumentModel(ContentDocument contentDocument) {
        InjectorHolder.getInjector().inject(this);

        this.path = contentDocument.getPath();
    }

    @Override
    protected ContentDocument load() {
        return this.contentService.getContentDocumentByPath(this.path, Session.get().getLocale());
    }
}
