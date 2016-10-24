package com.thoughtworks.lean.exception;

/**
 * Created by yongliuli on 7/27/16.
 */
public class ServiceErrorException extends RuntimeException {
    public ServiceErrorException() {
        super();
    }

    public ServiceErrorException(String message) {
        super(message);
    }

    public ServiceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceErrorException(Throwable cause) {
        super(cause);
    }

    protected ServiceErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
