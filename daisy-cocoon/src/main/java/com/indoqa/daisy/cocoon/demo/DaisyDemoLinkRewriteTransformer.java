package com.indoqa.daisy.cocoon.demo;

import com.indoqa.daisy.dao.pipeline.DaisyLinkRewriteTransformer;

public class DaisyDemoLinkRewriteTransformer extends DaisyLinkRewriteTransformer {

    @Override
    protected String rewriteDocumentLink(LinkInfo linkInfo) {
        return this.getPathRelativizer() + "part/WebpagePartGerman/" + linkInfo.getNavigationPath();
    }
}
