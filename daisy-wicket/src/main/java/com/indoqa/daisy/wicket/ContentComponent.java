package com.indoqa.daisy.wicket;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;

import com.indoqa.daisy.wicket.ContentDocumentModel;

public class ContentComponent extends WebComponent {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_PART_TYPE = "SimpleDocumentContent";

    private final String partType;
    private final ContentDocumentModel contentDocumentModel;

    public ContentComponent(String id, ContentDocumentModel contentDocumentModel) {
        this(id, contentDocumentModel, DEFAULT_PART_TYPE);
    }

    public ContentComponent(String id, ContentDocumentModel contentDocumentModel, String partType) {
        super(id, contentDocumentModel);

        this.contentDocumentModel = contentDocumentModel;
        this.partType = partType;

        this.setRenderBodyOnly(true);
    }

    @Override
    protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        this.replaceComponentTagBody(markupStream, openTag, this.contentDocumentModel.getObject().getContentPart(this.partType)
                .getText());
    }
}
