package org.daisycms.clientapp.cocoon;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.cocoon.servlet.util.ServletServiceUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ResourceRepository {

    private String resourceDocPath;
    private Servlet resourceDocServlet;
    private Map<String, String> resourcesById;
    private Map<String, String> resourcesByPath;

    public Map<String, String> getResourcesById() throws Exception {
        if (this.resourcesById == null) {
            this.parseResourcesDocument();
        }

        return this.resourcesById;
    }

    public Map<String, String> getResourcesByPath() throws Exception {
        if (this.resourcesByPath == null) {
            this.parseResourcesDocument();
        }

        return this.resourcesByPath;
    }

    public void setResourceDocPath(String resourceDocPath) {
        this.resourceDocPath = resourceDocPath;
    }

    public void setResourceDocServlet(Servlet daisyServlet) {
        this.resourceDocServlet = daisyServlet;
    }

    private void parseResourcesDocument() throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        ResourceHandler resourceHandler = new ResourceHandler();
        saxParser.parse(ServletServiceUtils.getServletServiceResource(this.resourceDocServlet, this.resourceDocPath),
                resourceHandler);
        this.resourcesByPath = resourceHandler.getResources();
    }

    public class ResourceHandler extends DefaultHandler {

        private static final String DOCUMENT_ID_ATTR = "documentId";
        private static final String PATH_ATTR = "path";
        private static final String RESOURCE_EL = "resource";

        private Map<String, String> resources = new HashMap<String, String>();

        public Map<String, String> getResources() {
            return this.resources;
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, name, attributes);
            if (RESOURCE_EL.equals(localName)) {
                String label = attributes.getValue(PATH_ATTR);
                String documentId = attributes.getValue(DOCUMENT_ID_ATTR);
                this.resources.put(label, documentId);
                this.resources.put(documentId, label);
            }
        }
    }
}
