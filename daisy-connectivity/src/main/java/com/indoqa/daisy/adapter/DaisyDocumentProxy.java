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
package com.indoqa.daisy.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.lang.Validate;
import org.outerj.daisy.repository.Document;
import org.outerj.daisy.xmlutil.XmlSerializer;
import org.xml.sax.ContentHandler;

import com.indoqa.daisy.adapter.pr.PRDocumentConfiguration;
import com.indoqa.daisy.adapter.pr.PublisherRequestDocumentBuilder;

public class DaisyDocumentProxy implements DaisyDocument {

    private Document jDocument;
    private DaisyRepositoryAccessFacade repoAccessFacade;
    private String docId;
    private String branchId;
    private String languageId;
    private PRDocumentConfiguration prConfig;

    public DaisyDocumentProxy(final String docId, final DaisyRepositoryAccessFacade docAccessFacade) {
        this(docId, docAccessFacade, new PRDocumentConfiguration());
    }

    public DaisyDocumentProxy(final String docId, final DaisyRepositoryAccessFacade docAccessFacade,
            final PRDocumentConfiguration prConfig) {
        this(docId, docAccessFacade, prConfig, null, null);
    }

    public DaisyDocumentProxy(final String docId, final DaisyRepositoryAccessFacade docAccessFacade,
            final PRDocumentConfiguration prConfig, final String branchId, final String languageId) {

        Validate.notNull(docAccessFacade, "A DocumentAccessFacade object has to be passed.");
        Validate.notNull(prConfig, "The publisher request configuration mustn't be null");

        this.docId = docId;
        this.branchId = branchId;
        this.languageId = languageId;
        this.repoAccessFacade = docAccessFacade;
        this.prConfig = prConfig;

        if (this.branchId == null) {
            this.branchId = "1";
        }

        if (this.languageId == null) {
            this.languageId = "1";
        }
    }

    public static String createUniqeFileName(DaisyDocument doc) {
        return doc.getDocId() + "_" + doc.getBranchId() + "_" + doc.getLanguageId();
    }

    public byte[] asByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            this.toSax(new XmlSerializer(baos));
        } catch (Exception e) {
            throw new DaisyClientException("A problem occurred while serializing the document returned from the publisher.", e);
        }
        return baos.toByteArray();
    }

    public String asString() {
        return new String(this.asByteArray());
    }

    public String getBranchId() {
        return this.branchId;
    }

    public String getDocId() {
        return this.docId;
    }

    public Document getDocument() {
        if (this.jDocument == null) {
            this.jDocument = this.repoAccessFacade.getJDocument(this.docId, this.branchId, this.languageId);
        }
        return this.jDocument;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.asByteArray());
    }

    public String getLanguageId() {
        return this.languageId;
    }

    public boolean isNavigationDocument() {
        return this.prConfig.isNavigationDocument;
    }

    public void toSax(ContentHandler contentHandler) {
        this.repoAccessFacade.getPublisherDocument(
                PublisherRequestDocumentBuilder.createPublisherRequestDocument(this, this.prConfig), contentHandler);
    }

    @Override
    public String toString() {
        return DaisyDocumentProxy.class.getName() + "[id=" + this.docId + " branchId=" + this.branchId + " languageId="
                + this.languageId + "]";
    }
}
