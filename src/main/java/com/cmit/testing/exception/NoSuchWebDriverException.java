package com.cmit.testing.exception;

/**
 * Created by Suviky on 2018/7/18 9:50
 */
public class NoSuchWebDriverException extends RuntimeException{
    public NoSuchWebDriverException() {
    }

    public NoSuchWebDriverException(String message) {
        super(message);
    }

    public NoSuchWebDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchWebDriverException(Throwable cause) {
        super(cause);
    }

    public NoSuchWebDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
