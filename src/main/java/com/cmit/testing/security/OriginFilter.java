package com.cmit.testing.security;

import com.cmit.testing.entity.vo.WhiteListVO;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: create by YangWanLi
 * @version: v1.0
 * @description: com.cmit.testing.security 白名单校验
 * @date:2018/12/11
 */
public class OriginFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(servletRequest);
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        String myOrigin = httpRequest.getHeader("origin");

        if(getWhiteList().contains(myOrigin)) { //跨域校验
            httpResponse.setHeader("Access-control-Allow-Origin", myOrigin);
            httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE");
            httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
            httpResponse.setStatus(HttpStatus.OK.value());
        }
        return  true;

    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    /**
     * 获取白名单
     * @return
     */
    private List<String> getWhiteList(){
        List<String> origins = new ArrayList<>();
        Map<String,WhiteListVO> map = (Map<String,WhiteListVO>)RedisUtil.getObject(Constants.ORIGIN_KEY);
        if(map == null || map.isEmpty()){
            map = initWhiteList();
            RedisUtil.setObject(Constants.ORIGIN_KEY,map);
        }
        for (WhiteListVO v : map.values()) {
            origins.add(v.getIp());
        }
        return origins;
    }

    /**
     * 初始化白名单
     * @return
     */
    public Map<String,WhiteListVO> initWhiteList(){
        Map<String,WhiteListVO> map = new HashMap<>();

        String id = StringUtils.generateUUID();
        map.put(id,new WhiteListVO(id,"https://yhmncs.chinamobilesz.com:8080","Nginx服务器"));

        id = StringUtils.generateUUID();
        map.put(id,new WhiteListVO(id,"http://192.168.41.67:5000","前端服务器"));

        return map;
    }
}
