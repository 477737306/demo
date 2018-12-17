package com.cmit.testing.exception;


import com.cmit.testing.utils.JsonResultUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class CtrlExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(CtrlExceptionHandler.class);

    //拦截未授权页面
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleException(UnauthorizedException e) {
        logger.debug(e.getMessage());
        return "403";
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public String handleException2(AuthorizationException e) {
        logger.debug(e.getMessage());
        return "403";
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(TestSystemException.class)
    @ResponseBody
    public JsonResultUtil handleException3(TestSystemException e) {
        logger.debug(e.getMessage());
        return new JsonResultUtil(300,e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED,reason = "请重新登录！")
    @ExceptionHandler(TestUnauthorizedException.class)
    @ResponseBody
    public JsonResultUtil handleException4(TestUnauthorizedException e) {
        logger.debug(e.getMessage());
        return new JsonResultUtil(401,"请重新登录！");
    }
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED,reason = "无权限！")
    @ExceptionHandler(TestNoPermissionException.class)
    @ResponseBody
    public JsonResultUtil handleException5(TestNoPermissionException e) {
        logger.debug(e.getMessage());
        return new JsonResultUtil(405,"无权限！");
    }

    @ExceptionHandler(NoForeignPermissionException.class)
    @ResponseBody
    public JsonResultUtil handleException6(NoForeignPermissionException e) {
        logger.debug(e.getMessage());
        return new JsonResultUtil(9999,e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,reason = "系统内部错误！")
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResultUtil handleException7(Exception e) {
        //e.printStackTrace();
        logger.error(e.getMessage(), e);
        return new JsonResultUtil(500,"系统内部错误！");
    }
}