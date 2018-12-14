package com.cmit.testing.exception;

/**
 * 验证码错误异常
 * Created by Suviky on 2018/7/10 9:50
 */
public class VcodeException extends RuntimeException {
    public VcodeException() {
        super();
    }

    public VcodeException(String message) {
        super(message);
    }

    public VcodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public VcodeException(Throwable cause) {
        super(cause);
    }

    protected VcodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
