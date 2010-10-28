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
import java.io.File;
import java.io.FileOutputStream;

import com.indoqa.daisy.adapter.pr.PRDocumentConfiguration;
import com.indoqa.daisy.adapter.transformer.impl.ResourceXsltTransformerSource;
import com.indoqa.daisy.adapter.util.XMLUtils;

public class DaisyDocumentIntegrationTests extends IntegrationTest {

    public void ittest_IT_extractAllLinks() throws Exception {
        if (!this.on) {
            return;
        }

        DaisyDocument doc = new DaisyDocumentProxy("489-IDQ", this.createDocumentAccessFacade());
        XMLUtils.transform(new ByteArrayInputStream(doc.asByteArray()), new FileOutputStream(new File("sample-extractedLinks.xml")),
                new ResourceXsltTransformerSource("org/daisycms/client/stylesheets/extractLinks.xslt"), null);
    }

    public void test_IT_DocumentWithImgAndLinks() throws Exception {
        if (!this.on) {
            return;
        }

        DaisyDocument doc = new DaisyDocumentProxy("489-IDQ", this.createDocumentAccessFacade());
        FileOutputStream fos = new FileOutputStream(new File("sample-img.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }

    public void test_IT_DocumentWithPre() throws Exception {
        if (!this.on) {
            return;
        }

        DaisyDocument doc = new DaisyDocumentProxy("856-IDQ", this.createDocumentAccessFacade());
        FileOutputStream fos = new FileOutputStream(new File("sample-docwithquery.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }

    public void test_IT_DocumentWithQuery() throws Exception {
        if (!this.on) {
            return;
        }

        DaisyDocument doc = new DaisyDocumentProxy("1159-IDQ", this.createDocumentAccessFacade());
        FileOutputStream fos = new FileOutputStream(new File("sample-docwithpre.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }

    public void test_IT_NavigationDocument() throws Exception {
        if (!this.on) {
            return;
        }

        PRDocumentConfiguration config = new PRDocumentConfiguration();
        config.isNavigationDocument = true;
        DaisyDocument doc = new DaisyDocumentProxy("660-IDQ", this.createDocumentAccessFacade(), config);

        FileOutputStream fos = new FileOutputStream(new File("sample-navdoc1.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }

    public void test_IT_Transformation() throws Exception {
        if (!this.on) {
            return;
        }

        DaisyDocument doc = new DaisyDocumentProxy("659-IDQ", this.createDocumentAccessFacade());
        XMLUtils.transform(new ByteArrayInputStream(doc.asByteArray()), new FileOutputStream(new File("out.xml")),
                new ResourceXsltTransformerSource("org/daisycms/clientapp/facade/stylesheets/daisy.xslt"), null);
    }

    private DaisyRepositoryAccessFacade createDocumentAccessFacade() {
        DaisyRepositoryAccessFacade repoAccessFacade = new DaisyRepositoryAccessFacade();

        repoAccessFacade = new DaisyRepositoryAccessFacade();
        repoAccessFacade.setServerUrl("http://cocoon.zones.apache.org:9263");
        repoAccessFacade.setRemoteAccessCredentials(new DaisyCredentials("reinhard-admin", "xxx"));
        repoAccessFacade.setRepoCredentials(new DaisyCredentials("reinhard-admin", "xxx"));

        return repoAccessFacade;
    }
}
