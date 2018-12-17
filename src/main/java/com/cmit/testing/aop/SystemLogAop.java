/**
 * Copyright (c) 2015-2017, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmit.testing.aop;


import com.cmit.testing.common.annotion.SystemLog;
import com.cmit.testing.entity.Project;
import com.cmit.testing.entity.SysLog;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import com.cmit.testing.service.SysLogService;
import com.cmit.testing.utils.JsonResultUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * AOP 系统日志处理
 *
 * @author YangWanLi
 * @date 2018/7/9 16:29
 */
@Aspect
@Component
@Order(200)
public class SystemLogAop {
    public static Logger logger = LoggerFactory.getLogger(Class.class);

    @Autowired
    private SysLogService sysLogService;

    /**
     * 方法切点
     */
    @Pointcut(value = "@annotation(com.cmit.testing.common.annotion.SystemLog)")
    private void cutSystemLogMethod() {

    }

    /**
     * 类切点
     */
    @Pointcut(value = "@within(com.cmit.testing.common.annotion.SystemLog)")
    private void cutSystemLogClass() {

    }

  /**
     * 方法注解拦截处理
     * @param joinPoint
     * @param jsonResultUtil
     * @return
   */
    @AfterReturning(returning = "jsonResultUtil", pointcut = "cutSystemLogMethod()")
    public void doSystemLogMethodAround(JoinPoint joinPoint,JsonResultUtil jsonResultUtil) {
        //不影响业务
        try{
            saveSystemLog( joinPoint, jsonResultUtil.toJson());
        } catch (Exception ex) {
            logger.error("用户操作【拦截器】内部异常: ", ex);
        }
    }

  /**
     * 类拦注解截处理
     * @param joinPoint
     * @param jsonResultUtil
     * @return
   */
    @AfterReturning(returning = "jsonResultUtil", pointcut = "cutSystemLogClass()")
    public void doSystemLogClassAround(JoinPoint joinPoint,JsonResultUtil jsonResultUtil) {
        //不影响业务
        try{
            saveSystemLog( joinPoint, jsonResultUtil.toJson());
        } catch (Exception ex) {
            logger.error("用户操作【拦截器】内部异常: ", ex);
        }
    }

   /**
     * 类注解异常处理
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "cutSystemLogClass()", throwing = "e")
    public void afterSystemLogClassThrowing(JoinPoint joinPoint,Throwable e) {
        saveSystemLog(joinPoint,e.getMessage());
        logger.error("afterThrowing: "+e.getMessage(), e);
    }

  /**
     * 方法注解异常处理
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "cutSystemLogMethod()", throwing = "e")
    public void afterSystemLogMethodThrowing(JoinPoint joinPoint,Throwable e) {
        saveSystemLog(joinPoint,e.getMessage());
        logger.error("afterThrowing: "+e.getMessage(), e);
    }

    /**
     * 日志保存
     * @param joinPoint
     * @param result
     */
    public void saveSystemLog(JoinPoint joinPoint,String result){
        Signature signature = joinPoint.getSignature();
        Class declaringType = signature.getDeclaringType();
        SystemLog systemLog = (SystemLog) declaringType.getAnnotation(SystemLog.class);

        Object[] modelName = systemLog.value();//模块名

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取访问ip
        //获取访问ip
        String ip = request.getHeader("USER-IP");
        String queryString = request.getQueryString();//请求参数
        String url = request.getRequestURI();//请求URL
        String sessionId = request.getSession().getId();//请求sessionId
        String requestMethod = request.getMethod();//请求方式
        String data="[]";

        if ("GET".equalsIgnoreCase(requestMethod)) {
            requestMethod = "查询(" + requestMethod + ")";
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            requestMethod = "新增(" + requestMethod + ")";
            data = Arrays.toString(joinPoint.getArgs());
        } else if ("PUT".equalsIgnoreCase(requestMethod)) {
            requestMethod = "更新(" + requestMethod + ")";
            data = Arrays.toString(joinPoint.getArgs());
        } else if ("DELETE".equalsIgnoreCase(requestMethod)) {
            requestMethod = "删除(" + requestMethod + ")";
            data = Arrays.toString(joinPoint.getArgs());
        }
        String class_method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
//        logger.info("-----操作类型: {}", requestMethod);
//        logger.info("-----请求模块: "+modelName[0].toString());
//        logger.info("-----请求url: " + url );
//        logger.info("-----请求目标：" + class_method );
//        logger.info("-----请求参数: " + queryString);
//        logger.info("-----请求体: " + data);
//        logger.info("-----返回结果: " + result);


        String operationInfo = "请求url: " + url+
                "\n请求目标：" + class_method+
                "\n请求参数: " + queryString+
                "\n请求体: " + data;
        ShiroUser shiroUser = ShiroKit.getUser();

        //保存日志
        SysLog sysLog = new SysLog();
        sysLog.setModelName(modelName[0].toString());
        sysLog.setOperationInfo(operationInfo);
        sysLog.setOperationType(requestMethod);
        sysLog.setServerIp(ip);
        sysLog.setSessionId(sessionId);
        sysLog.setCreateBy(shiroUser.getAccount());
        sysLog.setUpdateBy(shiroUser.getAccount());
        sysLog.setCreateTime(new Date());
        sysLog.setUpdateTime(new Date());
        sysLog.setResult(result);
        sysLogService.insert(sysLog);
    }
}
