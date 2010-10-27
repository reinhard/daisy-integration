package com.indoqa.daisy.dao;

public class DaisyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DaisyException() {
        super();
    }

    public DaisyException(String msg) {
        super(msg);
    }

    public DaisyException(String msg, Throwable t) {
        super(msg, t);
    }

    public DaisyException(Throwable t) {
        super(t);
    }
}
