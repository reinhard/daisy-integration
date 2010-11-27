package com.indoqa.daisy.pipeline;

import java.util.Map;

import org.apache.cocoon.sax.AbstractSAXTransformer;
import org.apache.cocoon.xml.sax.SAXBuffer;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class DaisyLinkRewriteTransformer extends AbstractSAXTransformer {

    private static final String DEFAULT_DATA_URL = "data/{id}/{filename}";
    private static final String DEFAULT_IMG_URL = "image/{id}/{filename}";
    private static final String LINK_INFO_EL = "linkInfo";

    private static final String LINK_PART_INFO_EL = "linkPartInfo";
    private static final String PUBLISHER_NS = "http://outerx.org/daisy/1.0#publisher";

    private StartElement currentElement;
    private LinkInfo currentLinkInfo;
    private String currentPath;
    private String dataUrl = DEFAULT_DATA_URL;
    private String imageUrl = DEFAULT_IMG_URL;
    private final Map<String, String> linkRewriteTranslationTable;

    public DaisyLinkRewriteTransformer() {
        this(null, null);
    }

    public DaisyLinkRewriteTransformer(String currentPath, Map<String, String> linkRewriteTranslationTable) {
        super();
        this.currentPath = currentPath;
        this.linkRewriteTranslationTable = linkRewriteTranslationTable;
    }

    @Override
    public final void endElement(String uri, String localName, String name) throws SAXException {
        if (PUBLISHER_NS.equals(uri)) {
            return;
        }

        if (this.currentElement != null && (localName.equals("a") || localName.equals("img"))) {
            SAXBuffer saxBuffer = this.endSAXRecording();
            super.startElement(this.currentElement.getUri(), this.currentElement.getLocalName(), this.currentElement.getName(), this
                    .rewriteAttributes(this.currentElement.getAtttributes(), this.currentLinkInfo));
            saxBuffer.toSAX(this.getSAXConsumer());
            this.currentElement = null;
        }

        super.endElement(uri, localName, name);
    }

    @Override
    public void setConfiguration(Map<String, ? extends Object> configuration) {
        super.setConfiguration(configuration);

        this.currentPath = (String) configuration.get("currentPath");
        this.imageUrl = replaceIfNotNull(configuration.get("imageUrl"), this.imageUrl);
        this.dataUrl = replaceIfNotNull(configuration.get("dataUrl"), this.dataUrl);
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        // collect link information
        if (PUBLISHER_NS.equals(uri) && LINK_INFO_EL.equals(localName) && this.currentLinkInfo != null) {
            this.currentLinkInfo.setSource(atts.getValue("src"));
            this.currentLinkInfo.setHref(atts.getValue("href"));
            this.currentLinkInfo.setDocumentType(atts.getValue("documentType"));

            String documentType = atts.getValue("documentType");
            if (StringUtils.isBlank(documentType)) {
                return;
            }

            if (documentType.equals("Attachment")) {
                this.currentLinkInfo.setLinkInfoType(LinkInfoType.ATTACHEMENT);
            } else if (documentType.equals("Image")) {
                this.currentLinkInfo.setLinkInfoType(LinkInfoType.IMAGE);
            }
            return;
        }

        // collection link part information
        if (this.currentLinkInfo != null && PUBLISHER_NS.equals(uri) && LINK_PART_INFO_EL.equals(localName)) {
            // if (this.currentLinkInfo == null) {
            // throw new ProcessingException(new NullPointerException("The LinkInfo object mustn't be null here."));
            // }

            String fileName = atts.getValue("fileName");
            if (fileName != null) {
                this.currentLinkInfo.setFileName(fileName);
            }

            return;
        }

        // other publisher stuff
        if (PUBLISHER_NS.equals(uri)) {
            return;
        }

        // record 'a' and 'img' tags of Daisy links
        if (localName.equals("a") || localName.equals("img")) {
            String href = atts.getValue("href");
            String src = atts.getValue("src");
            if (href != null && href.startsWith("daisy:") || src != null && src.startsWith("daisy")) {
                this.currentLinkInfo = new LinkInfo();
                this.currentLinkInfo.setNavigationPath(atts.getValue(PUBLISHER_NS, "navigationPath"));

                this.currentElement = new StartElement(uri, localName, name, atts);
                this.startSAXRecording();

                return;
            }
        }

        super.startElement(uri, localName, name, atts);
    }

    protected String getPathRelativizer() {
        if (StringUtils.isBlank(this.currentPath)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < StringUtils.countMatches(this.currentPath, "/"); i++) {
            sb.append("../");
        }

        return sb.toString();
    }

    protected String replaceVariables(String expression, LinkInfo linkInfo) {
        String result = expression;

        result = result.replace("{id}", linkInfo.getDocumentId());
        result = result.replace("{filename}", linkInfo.getFileName());

        return result;
    }

    protected String rewriteAttachementLink(LinkInfo linkInfo) {
        return this.getPathRelativizer() + this.replaceVariables(this.dataUrl, linkInfo);
    }

    protected String rewriteClassAttribute(LinkInfo linkInfo, String initialClassValue) {
        if (linkInfo.getDocumentType() == null) {
            return "";
        }

        String daisyClass = "daisy-document-" + linkInfo.getDocumentType().toLowerCase();
        if (StringUtils.isBlank(initialClassValue)) {
            return daisyClass;
        }

        return initialClassValue + " " + daisyClass;
    }

    protected String rewriteDocumentLink(LinkInfo linkInfo) {
        return this.getPathRelativizer() + this.translateLink(linkInfo.getNavigationPath());
    }

    protected String rewriteImageLink(LinkInfo linkInfo) {
        return this.getPathRelativizer() + this.replaceVariables(this.imageUrl, linkInfo);
    }

    protected String rewriteLinkAttribute(LinkInfo linkInfo) {
        if (linkInfo.getLinkInfoType() == LinkInfoType.IMAGE) {
            return this.rewriteImageLink(linkInfo);
        }

        if (linkInfo.getLinkInfoType() == LinkInfoType.ATTACHEMENT) {
            return this.rewriteAttachementLink(linkInfo);
        }

        return this.rewriteDocumentLink(linkInfo);
    }

    protected String translateLink(String oldLink) {
        if (this.linkRewriteTranslationTable == null) {
            return oldLink;
        }

        String newLink = this.linkRewriteTranslationTable.get(oldLink);

        if (StringUtils.isBlank(newLink)) {
            return oldLink;
        }

        return newLink;
    }

    private Attributes rewriteAttributes(Attributes atts, LinkInfo linkInfo) {
        AttributesImpl rewrittenAtts = new AttributesImpl();

        String classValue = "";
        for (int i = 0; i < atts.getLength(); i++) {
            String eachUri = atts.getURI(i);
            String eachLocalName = atts.getLocalName(i);
            String eachQName = atts.getQName(i);
            String eachType = atts.getType(i);
            String eachValue = atts.getValue(i);

            // remove all Daisy specific attributes
            if (PUBLISHER_NS.equals(eachUri)) {
                continue;
            }

            // rewrite links
            if (eachLocalName.equals("href") || eachLocalName.equals("src")) {
                eachValue = this.rewriteLinkAttribute(linkInfo);
            }

            if (eachLocalName.equals("class")) {
                classValue = eachValue;
            }

            // add the attribute to the attributes list
            rewrittenAtts.addAttribute(eachUri, eachLocalName, eachQName, eachType, eachValue);
        }

        // add class with document name
        rewrittenAtts.addAttribute("", "class", "class", "PCDATA", this.rewriteClassAttribute(linkInfo, classValue));

        return rewrittenAtts;
    }

    private static String replaceIfNotNull(Object parameter, String defaultValue) {
        if (parameter == null) {
            return defaultValue;
        }

        return (String) parameter;
    }

    protected static class LinkInfo {

        private String documentType;
        private String fileName;
        private String href;
        private LinkInfoType linkInfoType = LinkInfoType.MISC;
        private String navigationPath;
        private String source;

        public String getDocumentId() {
            if (this.href != null && this.href.startsWith("daisy:")) {
                return this.href.substring("daisy:".length());
            }

            if (this.source != null && this.source.startsWith("daisy:")) {
                return this.source.substring("daisy:".length());
            }

            return null;
        }

        public String getDocumentType() {
            return this.documentType;
        }

        public String getFileName() {
            return this.fileName;
        }

        public String getHref() {
            return this.href;
        }

        public LinkInfoType getLinkInfoType() {
            return this.linkInfoType;
        }

        public String getNavigationPath() {
            return this.navigationPath;
        }

        public String getSource() {
            return this.source;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setLinkInfoType(LinkInfoType linkInfoType) {
            this.linkInfoType = linkInfoType;
        }

        public void setNavigationPath(String navigationPath) {
            String result = navigationPath;

            if (navigationPath != null && navigationPath.startsWith("/")) {
                result = navigationPath.substring(1);
            }

            this.navigationPath = result;
        }

        public void setSource(String source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return "LinkInfo[documentId=" + this.getDocumentId() + ", navigationPath=" + this.navigationPath + ", source="
                    + this.source + ", href=" + this.href + ", fileName=" + this.fileName + "]";
        }
    }

    protected static enum LinkInfoType {
        ATTACHEMENT, IMAGE, MISC
    }

    private static class StartElement {

        private Attributes atttributes;
        private String localName;
        private String name;
        private String uri;

        public StartElement(String uri, String localName, String name, Attributes atttributes) {
            super();

            this.uri = uri;
            this.localName = localName;
            this.name = name;
            this.atttributes = new AttributesImpl(atttributes);
        }

        public Attributes getAtttributes() {
            return this.atttributes;
        }

        public String getLocalName() {
            return this.localName;
        }

        public String getName() {
            return this.name;
        }

        public String getUri() {
            return this.uri;
        }
    }
}
