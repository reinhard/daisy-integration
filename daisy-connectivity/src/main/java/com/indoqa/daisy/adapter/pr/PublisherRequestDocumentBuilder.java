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
package com.indoqa.daisy.adapter.pr;

import org.apache.commons.lang.Validate;
import org.outerx.daisy.x10Publisher.PublisherRequestDocument;
import org.outerx.daisy.x10Publisher.VariantKeyType;
import org.outerx.daisy.x10Publisher.NavigationTreeDocument.NavigationTree;
import org.outerx.daisy.x10Publisher.PreparedDocumentsDocument.PreparedDocuments;
import org.outerx.daisy.x10Publisher.PublisherRequestDocument.PublisherRequest;

import com.indoqa.daisy.adapter.DaisyDocumentProxy;

public class PublisherRequestDocumentBuilder {

    public static PublisherRequestDocument createPublisherRequestDocument(final DaisyDocumentProxy proxy,
            final PRDocumentConfiguration config) {
        final String id = proxy.getDocId();
        final String branchId = proxy.getBranchId();
        final String languageId = proxy.getLanguageId();

        PublisherRequestDocument prDoc = PublisherRequestDocument.Factory.newInstance();

        Validate.notNull(id, "You have to pass the document id.");
        Validate.notNull(branchId, "You have to pass the branch id.");
        Validate.notNull(languageId, "You have to pass a language id.");

        PublisherRequest pr = prDoc.addNewPublisherRequest();
        if (config.useLast) {
            pr.setVersionMode("last");
        }

        if (!config.isNavigationDocument) {
            org.outerx.daisy.x10Publisher.DocumentDocument.Document doc = pr.addNewDocument();
            doc.setId(id);
            doc.setBranch(branchId);
            doc.setLanguage(languageId);
            if (config.useLast) {
                doc.setVersion("last");
            }

            PreparedDocuments preparedDocuments = doc.addNewPreparedDocuments();
            preparedDocuments.setApplyDocumentTypeStyling(true);

            if (config.annotatingNavDocId != null) {
                VariantKeyType navDoc = preparedDocuments.addNewNavigationDocument();
                navDoc.setId(config.annotatingNavDocId);
                navDoc.setBranch(config.annotatingNavDocBranchId);
                navDoc.setLanguage(config.annotatingNavDocLanguageId);
            }

        } else {
            NavigationTree navTree = pr.addNewNavigationTree();
            VariantKeyType navDoc = navTree.addNewNavigationDocument();
            navDoc.setId(id);
            navDoc.setBranch(branchId);
            navDoc.setLanguage(languageId);
            navTree.setContextualized(false);
        }
        return prDoc;
    }

}
