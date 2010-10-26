package org.daisycms.clientapp.cocoon.it;

import java.net.URL;

import org.apache.cocoon.tools.it.HtmlUnitTestCase;

public abstract class DaisyCocoonIntegrationTest extends HtmlUnitTestCase {

    private static final String BASEURL = "http://localhost:8891/";

    @Override
    protected URL setupBaseUrl() throws Exception {
        String baseUrl = System.getProperty("htmlunit.base-url");

        if (baseUrl == null) {
            baseUrl = BASEURL;
        }

        return new URL(baseUrl);
    }
}
