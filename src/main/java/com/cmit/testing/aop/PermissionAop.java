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


import com.cmit.testing.common.annotion.Permission;
import com.cmit.testing.exception.TestNoPermissionException;
import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.check.PermissionCheckManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP 权限自定义检查
 *
 * @author YangWanLi
 * @date 2018/7/9 16:29
 */
@Aspect
@Component
@Order(200)
public class PermissionAop {

    /**
     * 方法切入点
     */
    @Pointcut(value = "@annotation(com.cmit.testing.common.annotion.Permission)")
    private void cutPermission() {

    }

    /**
     * 类切点入点
     */
    @Pointcut(value = "@within(com.cmit.testing.common.annotion.Permission)")
    private void cutPermissionClass() {

    }

    @Before(value = "execution(* com.cmit.testing.web..*.*(..))")
    public void flushSession(JoinPoint joinPoint) {
        ShiroKit.getSession().setTimeout(30 * 60 * 1000);
    }

    @Around("cutPermission()")
    public Object doPermission(ProceedingJoinPoint point) throws Throwable {
        return permissionPoint(point);
    }

    @Around("cutPermissionClass()")
    public Object doPermissionClass(ProceedingJoinPoint point) throws Throwable {
        return permissionPoint(point);
    }

    /**
     * 权限校验
     *
     * @param point
     * @return
     * @throws Throwable
     */
    public Object permissionPoint(ProceedingJoinPoint point) throws Throwable {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Permission permission = method.getAnnotation(Permission.class);
        Object[] permissions = null;
        if (permission != null) {
            permissions = permission.marks();
        }
        String account = ShiroKit.getUser().getAccount();
        if ("admin".equals(account)) {//如果用户账号是admin拥有所有权限
            return point.proceed();
        }
        if (permissions == null || permissions.length == 0) {//如果没有指定校验的value
            //检查全体权限
            boolean result = PermissionCheckManager.checkAll();
            if (result) {
                return point.proceed();
            } else {
                throw new TestNoPermissionException();
            }
        } else {
            //检查指定角色
            boolean result = PermissionCheckManager.check(permissions);
            if (result) {
                return point.proceed();
            } else {
                throw new TestNoPermissionException();
            }
        }
    }
}
