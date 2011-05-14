package com.indoqa.daisy.wicket;

import java.util.Map;

import org.apache.cocoon.sitemap.util.WildcardMatcherHelper;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WicketURLDecoder;

/**
 * Use this class to test a wildcard expression whether it matches the current request path provided by Wicket's
 * RequestCycle request object.
 * 
 * After the test the {@link #get(int)} method can be used to retrieve the value for a wildcard.
 */
/*
 * FIXME To be moved to Indoqa Commons Wicket as soon as the WildcardMatcherHelper is moved to cocoon-utils
 */
public class WicketPathMatcher {

    private String path;
    private Map<String, String> result;
    private boolean test;

    public WicketPathMatcher() {
        this.path = WicketURLDecoder.PATH_INSTANCE.decode(RequestCycle.get().getRequest().getPath());

        if (this.path.endsWith(".html")) {
            this.path = this.path.substring(0, this.path.length() - ".html".length());
        }
    }

    public String get(int pos) {
        if (!this.test) {
            throw new IllegalStateException("First test() has to be called.");
        }

        if (this.result == null) {
            return null;
        }

        return this.result.get(Integer.toString(pos));

    }

    public String getPath() {
        return this.path;
    }

    public boolean test(String expr) {
        this.test = true;
        this.result = WildcardMatcherHelper.match(expr, this.path);

        if (this.result == null || this.result.isEmpty()) {
            return false;
        }

        return true;
    }
}
