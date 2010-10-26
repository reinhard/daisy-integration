package org.daisycms.clientapp.cocoon.demo;

import org.daisycms.clientapp.cocoon.DaisyLinkRewriteTransformer;

public class DaisyDemoLinkRewriteTransformer extends DaisyLinkRewriteTransformer {

    @Override
    protected String rewriteDocumentLink(LinkInfo linkInfo) {
        return this.getPathRelativizer() + "part/WebpagePartGerman/" + linkInfo.getNavigationPath();
    }
}
