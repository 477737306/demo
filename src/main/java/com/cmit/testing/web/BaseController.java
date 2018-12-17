package com.cmit.testing.web;

import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * Controller 基础类
 *
 * @author YangWanLi
 * @date 2018/7/5 16:54
 */
//@CrossOrigin(origins ={"https://yhmncs.chinamobilesz.com:8080","http://192.168.41.67:5000","http://192.168.215.89:3000","https://192.168.1.116:3001","http://192.168.215.47:3001","http://192.168.1.113:3000","https://192.168.41.67:5000","http://192.168.126.193:8181","http://192.168.207.201:8080","http://192.168.1.108:8080","http://192.168.1.107:3000","http://192.168.157.93:8080","http://192.168.215.199:3001", "http://192.168.215.112:3000"}, maxAge = 3600,allowCredentials = "true")
//@CrossOrigin
public class BaseController {

    public static Logger logger = LoggerFactory.getLogger(Class.class);


    public static final String SUPER_ADMIN="超级管理员";

    /**
     * 获取当前登录用户信息
     * @return
     */
    public ShiroUser getShiroUser(){
        return ShiroKit.getUser();
    }

    /**
     * 获取Session
     * @return
     */
    public Session getSession(){
        return ShiroKit.getSession();
    }

    public Subject getSubJect(){
        return ShiroKit.getSubject();
    }
}
