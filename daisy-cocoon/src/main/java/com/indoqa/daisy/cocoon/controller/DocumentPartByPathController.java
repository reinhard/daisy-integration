package com.indoqa.daisy.cocoon.controller;

import org.apache.cocoon.rest.controller.annotation.SitemapParameter;
import org.apache.cocoon.rest.controller.response.RestResponse;
import org.apache.cocoon.rest.controller.response.URLResponse;

public class DocumentPartByPathController extends AbstractDocumentController {

    @SitemapParameter
    private String part;

    @Override
    public RestResponse sendSuccessResponse(String id) throws Exception {
        return new URLResponse("/default/doc/id/" + id + "/part/" + this.part + ".html");
    }
}
