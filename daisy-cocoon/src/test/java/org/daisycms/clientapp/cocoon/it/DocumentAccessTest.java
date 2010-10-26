package org.daisycms.clientapp.cocoon.it;

/**
 * These integration tests use the documents 632 - 638 of the Indoqa Daisy CMS (site: indoqa.com).
 */
public class DocumentAccessTest extends DaisyCocoonIntegrationTest {

    public void testAccessDocumentById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/doc/id/633.xml");
    }

    public void testAccessDocumentPartById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/doc/id/632/part/WebpagePartGerman.html");
    }

    public void testAccessDocumentPartByPath() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/ext/part/WebpagePartGerman/folder1/folder2/page3.html");
    }

    public void testAccessPDFAttachementById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/ext/data/636/20090104-testdoc.pdf");
        String contentType = this.response.getResponseHeaderValue("Content-Type");
        assertEquals("application/pdf", contentType);
    }

    public void testAccessZIPAttachementById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/ext/data/637/20090104-testdoc.zip");
        String contentType = this.response.getResponseHeaderValue("Content-Type");
        assertEquals("application/zip", contentType);
    }

    public void testAccessImageById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/ext/image/638/daisy-test-doc.png");
        String contentType = this.response.getResponseHeaderValue("Content-Type");
        assertEquals("image/png", contentType);
    }

    public void testAccessSimpleNavDocDefault() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/nav-doc/default.simple.xml");
    }

    public void testAccessNavDocById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/nav-doc/id/635.xml");
    }

    public void testAccessSimpleNavDocById() throws Exception {
        this.isResourceAvailable("daisy-cocoon/default/nav-doc/id/635.simple.xml");
    }

    private void isResourceAvailable(String pageURL) throws Exception {
        this.loadResponse(pageURL);
        assertEquals(200, this.response.getStatusCode());
        assertNotNull(this.response.getContentAsString());
    }
}
