package com.cmit.testing.security.shiro;

import com.cmit.testing.utils.SpringContextHolder;
import com.cmit.testing.utils.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisSessionDAO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author YangWanLi
 * @date 2018/7/4 18:12
 */
public class MySessionManager extends DefaultWebSessionManager {

    private static final String AUTHORIZATION = "token";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public MySessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = "";

        HttpServletRequest hsr = (HttpServletRequest) request;
        String param = hsr.getHeader("Set-Cookie");
        if(StringUtils.isNotEmpty(param)) {
            String params[] = param.split(";");
            for (String param_ : params) {
                String[] a_ = param_.split("=");
                if (AUTHORIZATION.equals(a_[0])) {
                    id = a_[1];
                }
            }
        }
        //如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }

    }


    /**
     * 删除指定sessionId用户
     *
     * @param sessionId
     */
    public void deletSession(String sessionId) {
        RedisSessionDAO redisSessionDAO = SpringContextHolder.getBean("redisSessionDAO");

        Session session_ = redisSessionDAO.readSession(sessionId);

        SimplePrincipalCollection simplePrincipalCollection_new = (SimplePrincipalCollection) session_.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        ShiroUser shiroUser_new = new ShiroUser();
        try {
            shiroUser_new = (ShiroUser) simplePrincipalCollection_new.getPrimaryPrincipal();
        } catch (Exception e) {
            return;
        }
        //获取所有session，判断
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for (Session session : sessions) {
            SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

            if (session_.getId().equals(session.getId())) {
                continue;
            } else if (null == simplePrincipalCollection) {
                redisSessionDAO.delete(session);
            } else {
                ShiroUser shiroUser = (ShiroUser) simplePrincipalCollection.getPrimaryPrincipal();
                boolean bool = session_.getAttribute("browserName").toString().equals(session.getAttribute("browserName")) && session_.getAttribute("loginIp").toString().equals(session.getAttribute("loginIp"));
                if ((shiroUser_new.getAccount().equals(shiroUser.getAccount())) || bool) {
                    redisSessionDAO.delete(session);
                }
            }
        }
    }

    /**
     * 根据账号删除Session
     *
     * @param account
     */
    public void deletSessionByAccount(String account) {
        RedisSessionDAO redisSessionDAO = SpringContextHolder.getBean("redisSessionDAO");
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for (Session session : sessions) {
            SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (null == simplePrincipalCollection) {
                redisSessionDAO.delete(session);
            } else {
                ShiroUser shiroUser = (ShiroUser) simplePrincipalCollection.getPrimaryPrincipal();
                if ((account.equals(shiroUser.getAccount()))) {
                    redisSessionDAO.delete(session);
                }
            }
        }
    }

}
