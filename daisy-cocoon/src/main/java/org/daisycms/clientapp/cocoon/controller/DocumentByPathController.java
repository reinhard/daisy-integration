package org.daisycms.clientapp.cocoon.controller;

import org.apache.cocoon.rest.controller.response.RestResponse;
import org.apache.cocoon.rest.controller.response.URLResponse;

public class DocumentByPathController extends AbstractDocumentController {

    @Override
    public RestResponse sendSuccessResponse(String id) throws Exception {
        return new URLResponse("servlet:/default/doc/id/" + id + ".xml");
    }
}
