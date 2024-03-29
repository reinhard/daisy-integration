/*
 * Licensed to the Outerthought bvba and Schaubroeck NV under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding
 * copyright ownership.  Outerthought bvba and Schaubroeck NV license
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.indoqa.daisy.adapter.transformer.impl;

import java.io.IOException;
import java.io.InputStream;

import com.indoqa.daisy.adapter.transformer.XsltTransformerSource;

public class ResourceXsltTransformerSource implements XsltTransformerSource {

    private String url;

    public ResourceXsltTransformerSource(String url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return ResourceXsltTransformerSource.class.getClassLoader().getResource(this.url).openStream();
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}
