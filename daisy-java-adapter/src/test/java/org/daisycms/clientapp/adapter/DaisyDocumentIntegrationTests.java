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
package org.daisycms.clientapp.adapter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.daisycms.clientapp.adapter.DaisyCredentials;
import org.daisycms.clientapp.adapter.DaisyDocument;
import org.daisycms.clientapp.adapter.DaisyDocumentProxy;
import org.daisycms.clientapp.adapter.DaisyRepositoryAccessFacade;
import org.daisycms.clientapp.adapter.pr.PRDocumentConfiguration;
import org.daisycms.clientapp.adapter.transformer.impl.ResourceXsltTransformerSource;
import org.daisycms.clientapp.adapter.util.XMLUtils;

public class DaisyDocumentIntegrationTests extends IntegrationTest {
    
    private DaisyRepositoryAccessFacade createDocumentAccessFacade() {
        DaisyRepositoryAccessFacade repoAccessFacade = new DaisyRepositoryAccessFacade();
        repoAccessFacade = new DaisyRepositoryAccessFacade();
        repoAccessFacade.setServerUrl("http://cocoon.zones.apache.org:9263");
        repoAccessFacade.setRemoteAccessCredentials(new DaisyCredentials("reinhard-admin", "xxx"));
        repoAccessFacade.setRepoCredentials(new DaisyCredentials("reinhard-admin", "xxx"));
        return repoAccessFacade;
    }
    
    public void test_IT_NavigationDocument() throws Exception {
        if(!this.on) return;
        PRDocumentConfiguration config = new PRDocumentConfiguration();
        config.isNavigationDocument = true;
        DaisyDocument doc = new DaisyDocumentProxy(660, createDocumentAccessFacade(), config);
        FileOutputStream fos = new FileOutputStream(new File("sample-navdoc1.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }
    
    public void test_IT_DocumentWithPre() throws Exception {
        if(!this.on) return;        
        DaisyDocument doc = new DaisyDocumentProxy(856, createDocumentAccessFacade());
        FileOutputStream fos = new FileOutputStream(new File("sample-docwithquery.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }    
    
    public void test_IT_DocumentWithImgAndLinks() throws Exception {
        if(!this.on) return;        
        DaisyDocument doc = new DaisyDocumentProxy(489, createDocumentAccessFacade());
        FileOutputStream fos = new FileOutputStream(new File("sample-img.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }      
    
    public void test_IT_DocumentWithQuery() throws Exception {
        if(!this.on) return;        
        DaisyDocument doc = new DaisyDocumentProxy(1159, createDocumentAccessFacade());
        FileOutputStream fos = new FileOutputStream(new File("sample-docwithpre.xml"));
        fos.write(doc.asByteArray());
        fos.close();
    }       
    
    public void ittest_IT_extractAllLinks() throws Exception {
        if(!this.on) return;        
        DaisyDocument doc = new DaisyDocumentProxy(489, createDocumentAccessFacade());
        XMLUtils.transform(
            new ByteArrayInputStream(doc.asByteArray()),
            new FileOutputStream(new File("sample-extractedLinks.xml")),
            new ResourceXsltTransformerSource("org/daisycms/client/stylesheets/extractLinks.xslt"),
            null
        );
    }    
    
    public void test_IT_Transformation() throws Exception {
        if(!this.on) return;        
        DaisyDocument doc = new DaisyDocumentProxy(659, createDocumentAccessFacade());
        XMLUtils.transform(
            new ByteArrayInputStream(doc.asByteArray()),
            new FileOutputStream(new File("out.xml")),
            new ResourceXsltTransformerSource("org/daisycms/clientapp/facade/stylesheets/daisy.xslt"),
            null
        );
    }    
    
}
