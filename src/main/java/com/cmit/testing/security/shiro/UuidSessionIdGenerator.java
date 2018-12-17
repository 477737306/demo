package com.cmit.testing.security.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * 自定义生成SessionId
 * @author YangWanLi
 * @date 2018/7/18 10:55
 */
public class UuidSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        Serializable uuid = new JavaUuidSessionIdGenerator().generateId(session);
        return uuid;
    }

}
