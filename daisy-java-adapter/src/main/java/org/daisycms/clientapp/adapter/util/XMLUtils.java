/*
 * Licensed to the Outerthought bvba and Schaubroeck NV under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information  regarding 
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
package org.daisycms.clientapp.adapter.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.daisycms.clientapp.adapter.transformer.XsltTransformerSource;
import org.w3c.dom.Document;

public class XMLUtils {
    
    private static Map templatesMap = new HashMap();

    public static void transform(final InputStream xmlIn, final OutputStream xmlOut, 
            final XsltTransformerSource transformerSource, final Map params) {
        
        String xsltUrl = transformerSource.getUrl();        
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = (Templates) templatesMap.get(xsltUrl);
            if(template == null) {
                template = factory.newTemplates(new StreamSource(transformerSource.getInputStream()));         
                templatesMap.put(xsltUrl, template);
            }
            
            Transformer xsltTransformer = template.newTransformer();
            Source source = new StreamSource(xmlIn);
            Result result = new StreamResult(xmlOut);

            if(params != null) {
                for(Iterator it = params.keySet().iterator(); it.hasNext();) {
                    String key = (String) it.next();
                    Object value = params.get(key);
                    xsltTransformer.setParameter(key, value);
                }
            }
            
            xsltTransformer.transform(source, result);
            
        } catch (TransformerConfigurationException e) {
            throw new org.daisycms.clientapp.adapter.transformer.TransformerException("An error occurred in the XSL file (" + xsltUrl + ").", e);
            
        } catch (TransformerException e) {
            SourceLocator locator = e.getLocator();
            int col = locator.getColumnNumber();
            int line = locator.getLineNumber();
            throw new org.daisycms.clientapp.adapter.transformer.TransformerException("An error occurred while applying the XSLT file (" + 
                    xsltUrl + ") at line " + line + " in column " + col + ".", e);
            
        } catch (IOException e) {
            throw new org.daisycms.clientapp.adapter.transformer.TransformerException("An error occurred while reading the XSL file (" + xsltUrl + ").", e);
        }
    }    
    
    public static void writeDom(Document doc, File file) {
        try {
            Source source = new DOMSource(doc);
            Result result = new StreamResult(file);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new org.daisycms.clientapp.adapter.transformer.TransformerException("An error occurred while reading (" + file + ").", e);            
        } catch (TransformerException e) {
            throw new org.daisycms.clientapp.adapter.transformer.TransformerException("An error occurred while reading (" + file + ").", e);                
        }
    }        
    
}
