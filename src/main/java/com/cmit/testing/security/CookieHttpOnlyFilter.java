package com.cmit.testing.security;


import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * HttpOnly
 */
public class CookieHttpOnlyFilter extends AccessControlFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * 权限拦截方法
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String value = cookie.getValue();
                StringBuilder builder = new StringBuilder();
                builder.append("JSESSIONID=" + value + "; ");
                builder.append("Secure; ");
                builder.append("HttpOnly; ");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, 1);
                Date date = cal.getTime();
                Locale locale = Locale.CHINA;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
                builder.append("Expires=" + sdf.format(date));
                resp.setHeader("Set-Cookie", builder.toString());
            }
        }
        return true;
    }
}