package org.daisycms.clientapp.cocoon.it;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;

public class DaisyLinkRewriteTransformerTest extends DaisyCocoonIntegrationTest {

    public void testLinkRewriting() throws Exception {
        this.loadResponse("daisy-cocoon/default/ext/part/WebpagePartGerman/page1");
        assertEquals(200, this.response.getStatusCode());
        assertNotNull(this.response.getContentAsString());

        String result = this.response.getContentAsString();
        String correctOne = IOUtils.toString(this.getClass().getResource("daisy-document-result.xml").openStream());
        XMLUnit.setIgnoreWhitespace(true);
        Diff myDiff = new Diff(correctOne, result);
        assertTrue("pieces of XML are not similar: " + myDiff, myDiff.similar());
    }
}
