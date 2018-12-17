package com.cmit.testing.exception;

/**
 * 未授权异常，提示重新登陆
 * @author YangWanLi
 * @date 2018/7/18 17:16
 */
public class TestUnauthorizedException extends RuntimeException {
    public TestUnauthorizedException(){
        super();
    }

    public TestUnauthorizedException(String message) {
        super(message);
    }
}
