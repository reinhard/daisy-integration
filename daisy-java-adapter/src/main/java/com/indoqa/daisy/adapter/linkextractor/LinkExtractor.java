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
package com.indoqa.daisy.adapter.linkextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import com.indoqa.daisy.adapter.DaisyDocument;

public class LinkExtractor {
    
    protected static final String DAISY_LINK_PREFIX = "daisy";

    /**
     * Extract all links of a daisy document.
     */
    public static DaisyLink[] extract(final DaisyDocument doc) throws XmlException,
            IOException {     
        if(doc.isNavigationDocument()) {
            return extractNavigationTreeLinks(doc);
        }
        return extractDaisyHtmlLinks(doc);
    }

    /**
     * Extract all links from a Daisy navigation document.
     * @param parent 
     */
    private static DaisyLink[] extractNavigationTreeLinks(final DaisyDocument doc) 
        throws XmlException, IOException {
        
        List daisyLinks = new ArrayList();        
        XmlObject x = XmlObject.Factory.parse(doc.getInputStream());
        XmlCursor c = x.newCursor();

        String queryExpression = 
                  "declare namespace n='http://outerx.org/daisy/1.0#navigation';"
                + "$this//n:doc";
        c.selectPath(queryExpression); 
        while (c.hasNextSelection()) {
            c.toNextSelection();
            long docId = Long.parseLong(c.getAttributeText(new QName("documentId")));
            String branchId = c.getAttributeText(new QName("branchId"));
            String languageId = c.getAttributeText(new QName("languageId"));
            String label = c.getAttributeText(new QName("label"));    
            String path = c.getAttributeText(new QName("path"));            
            daisyLinks.add(new DaisyLink(docId, label, branchId, languageId, path, doc));
        }        
        return (DaisyLink[]) daisyLinks.toArray(new DaisyLink[daisyLinks.size()]);
    }

    /**
     * Extract all links from a Daisy document that contains Daisy HTML.
     * @param parent 
     */
    private static DaisyLink[] extractDaisyHtmlLinks(final DaisyDocument doc)
        throws XmlException, IOException {
        
        List daisyLinks = new ArrayList();

        // find all links in 'p:linkInfo'
        XmlObject x = XmlObject.Factory.parse(doc.getInputStream());
        XmlCursor c = x.newCursor();

        String queryExpression = 
                  "declare namespace p='http://outerx.org/daisy/1.0#publisher';"
                + "$this//p:linkInfo";
        c.selectPath(queryExpression);
        
        while (c.hasNextSelection()) {
            c.toNextSelection();
            String src = c.getAttributeText(new QName("src"));
            String href = c.getAttributeText(new QName("href"));
            String name = c.getAttributeText(new QName("documentName"));      
            
            if(src != null) {
                daisyLinks.add(createDaisyLink(src, name, doc));
            }
            else if(href != null) {
                daisyLinks.add(createDaisyLink(href, name, doc));
            }
        }
        
        // find all links in searchResults        
        c = x.newCursor();
        queryExpression = 
            "declare namespace d='http://outerx.org/daisy/1.0';"
          + "$this//d:searchResult/d:rows/d:row";
        c.selectPath(queryExpression);
        
        while (c.hasNextSelection()) {
            c.toNextSelection();
            String id = c.getAttributeText(new QName("documentId"));
            String branchId = c.getAttributeText(new QName("branchId"));
            String languageId = c.getAttributeText(new QName("languageId"));             
            DaisyLink link = new DaisyLink(Long.parseLong(id), "", branchId, languageId, null, doc); 
            daisyLinks.add(link);
        }

        return (DaisyLink[]) daisyLinks.toArray(new DaisyLink[daisyLinks.size()]);
    }

    /**
     * Parse a Daisy HTML link and create a @link DaisyLink object.
     * @param parent 
     */
    public static DaisyLink createDaisyLink(final String link, String name, final DaisyDocument parent) {
        if(!link.startsWith(DAISY_LINK_PREFIX)) {
            return null;
        }
        long id = 0;
        String branch = null;
        String language = null;
        String tempStr = link;
        int hashSign = tempStr.indexOf('#');
        if(hashSign > 0) {
            tempStr = tempStr.substring(0, hashSign);
        }
        tempStr = tempStr.substring(tempStr.indexOf(':') + 1);
        int atSign = tempStr.indexOf('@');
        if(atSign < 0) {
            id = Long.parseLong(tempStr);
        } else {
            // get the id
            id = Long.parseLong(tempStr.substring(0, atSign));
            tempStr = tempStr.substring(atSign + 1);
            // get the branch
            int nextColon = tempStr.indexOf(':');
            if(nextColon < 0) {
                branch = tempStr;
            } else {
                branch = tempStr.substring(0, tempStr.indexOf(':'));
                tempStr = tempStr.substring(tempStr.indexOf(':') + 1);
                // get the language
                language = tempStr;
            }
        }
        return new DaisyLink(id, name, branch, language, null, parent);
    }

}
