package com.wyht.dataengine.expection;

public class InternalException extends RuntimeException {
    public InternalException(Throwable cause) {
        super(cause);
    }

    public InternalException(String message) {
        super(message);
    }
}
