package com.indoqa.daisy.cocoon.controller;

import java.net.URLDecoder;

import org.apache.cocoon.rest.controller.annotation.SitemapParameter;
import org.apache.cocoon.rest.controller.method.Get;
import org.apache.cocoon.rest.controller.response.RestResponse;
import org.apache.cocoon.rest.controller.response.URLResponse;

import com.indoqa.daisy.cocoon.ResourceRepository;

public abstract class AbstractDocumentController implements Get {

    @SitemapParameter
    protected String path;

    private ResourceRepository repository;

    public RestResponse doGet() throws Exception {
        String decodedPath = URLDecoder.decode(this.path, "iso-8859-1");
        String id = this.repository.getResourcesByPath().get("/" + decodedPath);
        if (id == null) {
            return new URLResponse("servlet:/util/error/404.html");
        }

        return this.sendSuccessResponse(id);
    }

    public abstract RestResponse sendSuccessResponse(String id) throws Exception;

    public void setRepository(ResourceRepository repository) {
        this.repository = repository;
    }
}
