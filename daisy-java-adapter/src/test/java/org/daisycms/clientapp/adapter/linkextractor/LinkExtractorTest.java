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
package org.daisycms.clientapp.adapter.linkextractor;

import junit.framework.TestCase;

import org.daisycms.clientapp.adapter.DaisyDocument;
import org.daisycms.clientapp.adapter.linkextractor.DaisyLink;
import org.daisycms.clientapp.adapter.linkextractor.LinkExtractor;
import org.easymock.MockControl;

public class LinkExtractorTest extends TestCase {

    // ~~~~~~~~~~~~~~~~~~~~~~~~ test the link extraction ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void testLinkExtractionFromDaisyHtmlDocument() throws Exception {
        MockControl daisyDocCtrl = MockControl.createControl(DaisyDocument.class);
        DaisyDocument doc = (DaisyDocument) daisyDocCtrl.getMock();
        daisyDocCtrl.expectAndReturn(doc.getInputStream(), LinkExtractorTest.class.getResourceAsStream("linkextractor1.xml"));
        daisyDocCtrl.expectAndReturn(doc.isNavigationDocument(), false);
        daisyDocCtrl.replay();
        DaisyLink[] links = LinkExtractor.extract(doc);
        assertEquals(2, links.length);
        assertEquals(389, links[0].getDocId());
        assertEquals("1", links[0].getBranchId());
        assertEquals("1", links[0].getLanguageId());
        assertEquals("forms_schema", links[0].getName());
        assertEquals(doc, links[0].getParent());
        assertEquals(478, links[1].getDocId());
        assertEquals("1", links[1].getBranchId());
        assertEquals("1", links[1].getLanguageId());   
        System.out.println("code: " + links[0].hashCode());
    }
    
    public void testLinkExtractionFromNavigationDocument() throws Exception {
        MockControl daisyDocCtrl = MockControl.createControl(DaisyDocument.class);
        DaisyDocument doc = (DaisyDocument) daisyDocCtrl.getMock();
        daisyDocCtrl.expectAndReturn(doc.getInputStream(), LinkExtractorTest.class.getResourceAsStream("linkextractor2.xml"));
        daisyDocCtrl.expectAndReturn(doc.isNavigationDocument(), true);   
        daisyDocCtrl.replay();        
        DaisyLink[] links = LinkExtractor.extract(doc);
        
        assertEquals(9, links.length);
        assertEquals(706, links[8].getDocId());
        assertEquals("1", links[8].getBranchId());
        assertEquals("1", links[8].getLanguageId());
        assertEquals("Live Sites - 2.2", links[8].getName());
        assertEquals("/695/706", links[8].getPath());
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~ test the link parsing algorithm ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void testCreateDaisyLink_onlyId() {
        DaisyLink dl = LinkExtractor.createDaisyLink("daisy:4711", "blah", null);
        assertEquals(4711, dl.getDocId());
        assertEquals("1", dl.getBranchId());
        assertEquals("1", dl.getLanguageId());
    }

    public void testCreateDaisyLink_IdBranch() {
        DaisyLink dl = LinkExtractor.createDaisyLink("daisy:4711@mybranch", "blah", null);
        assertEquals(4711, dl.getDocId());
        assertEquals("mybranch", dl.getBranchId());
        assertEquals("1", dl.getLanguageId());
    }   
    
    public void testCreateDaisyLink_IdBranchLanguage() {
        DaisyLink dl = LinkExtractor.createDaisyLink("daisy:4711@mybranch:en", "blah", null);
        assertEquals(4711, dl.getDocId());
        assertEquals("mybranch", dl.getBranchId());
        assertEquals("en", dl.getLanguageId());
    }       
    
    public void testCreateDaisyLink_IdLanguage() {
        DaisyLink dl = LinkExtractor.createDaisyLink("daisy:4711@:en", "blah", null);
        assertEquals(4711, dl.getDocId());
        assertEquals("1", dl.getBranchId());
        assertEquals("en", dl.getLanguageId());
    }     
    
    public void testCreateDaisyLink_IdHash() {
        DaisyLink dl = LinkExtractor.createDaisyLink("daisy:518#FOM", "blah", null);
        assertEquals(518, dl.getDocId());
        assertEquals("1", dl.getBranchId());
        assertEquals("1", dl.getLanguageId());
    }         
    
}
