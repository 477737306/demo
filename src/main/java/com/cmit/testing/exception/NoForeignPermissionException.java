package com.cmit.testing.exception;

/**
 * 无权限异常
 * @author YangWanLi
 * @date 2018/7/18 17:16
 */
public class NoForeignPermissionException extends RuntimeException {
    public NoForeignPermissionException(){
        super();
    }

    public NoForeignPermissionException(String message) {
        super(message);
    }
}
