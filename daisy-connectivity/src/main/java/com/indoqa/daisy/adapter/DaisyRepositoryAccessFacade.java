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

import org.outerj.daisy.publisher.BlobInfo;
import org.outerj.daisy.publisher.Publisher;
import org.outerj.daisy.repository.Document;
import org.outerj.daisy.repository.RepositoryException;
import org.outerj.daisy.repository.VariantKey;
import org.outerj.daisy.repository.query.QueryManager;
import org.outerj.daisy.repository.user.UserManager;
import org.outerx.daisy.x10Publisher.PublisherRequestDocument;
import org.xml.sax.ContentHandler;

public class DaisyRepositoryAccessFacade extends AbstractDaisyRepositoryAccessFacade {

    public Document getJDocument(final String id) {
        return this.getJDocument(id, "1", "1");
    }

    public Document getJDocument(final String id, final String branchId, final String languageId) {
        Document jDocument = null;
        for (int i = 0; i <= this.getNumOfRetries(); i++) {
            try {
                jDocument = this.getRepository().getDocument(id, branchId, languageId, true);
                break;
            } catch (Exception e) {
                if (i == this.getNumOfRetries()) {
                    throw new DaisyAccessException("Problems occurred while accessing the Daisy repository server.", e);
                }
            }
        }
        return jDocument;
    }

    public BlobInfo getPartAsBlob(String documentId, long branchId, long languageId, String version, String partType) {
        BlobInfo blobInfo = null;

        for (int i = 0; i <= this.getNumOfRetries(); i++) {
            Publisher publisher = (Publisher) this.getRepository().getExtension("Publisher");
            try {
                blobInfo = publisher.getBlobInfo(new VariantKey(documentId, branchId, languageId), version, partType);
                break;
            } catch (RepositoryException e) {
                if (i == this.getNumOfRetries()) {
                    throw new DaisyClientException("A problem occurred while using the publisher.", e);
                }
            }
        }
        return blobInfo;
    }

    public void getPublisherDocument(PublisherRequestDocument prDoc, ContentHandler handler) {
        for (int i = 0; i <= this.getNumOfRetries(); i++) {
            Publisher publisher = (Publisher) this.getRepository().getExtension("Publisher");
            try {
                publisher.processRequest(prDoc, handler);
                break;
            } catch (Exception e) {
                if (i == this.getNumOfRetries()) {
                    throw new DaisyClientException("A problem occurred while using the publisher.", e);
                }
            }
        }
    }

    public QueryManager getQueryManager() {
        return this.getRepository().getQueryManager();
    }

    public UserManager getUserManager() {
        return this.getRepository().getUserManager();
    }
}
