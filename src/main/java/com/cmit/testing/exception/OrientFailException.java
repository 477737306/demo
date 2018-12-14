package com.cmit.testing.exception;

/**
 * Created by Suviky on 2018/7/18 11:19
 */
public class OrientFailException extends RuntimeException {
    public OrientFailException() {
    }

    public OrientFailException(String message) {
        super(message);
    }

    public OrientFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrientFailException(Throwable cause) {
        super(cause);
    }

    public OrientFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
