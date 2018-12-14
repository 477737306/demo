package com.cmit.testing.exception;

/**
 * 无权限异常
 * @author YangWanLi
 * @date 2018/7/18 17:16
 */
public class TestNoPermissionException extends RuntimeException {
    public TestNoPermissionException(){
        super();
    }

    public TestNoPermissionException(String message) {
        super(message);
    }
}
