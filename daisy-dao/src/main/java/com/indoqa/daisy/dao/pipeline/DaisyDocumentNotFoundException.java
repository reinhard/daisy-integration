package com.indoqa.daisy.dao.pipeline;

import org.apache.cocoon.pipeline.PipelineException;

public class DaisyDocumentNotFoundException extends PipelineException {

    private static final long serialVersionUID = 1L;

    public DaisyDocumentNotFoundException(String message) {
        super(message);
    }
}
