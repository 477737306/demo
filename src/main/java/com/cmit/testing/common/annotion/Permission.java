package com.cmit.testing.common.annotion;

import java.lang.annotation.*;

/**
 * 权限注解 用于检查权限 规定访问权限
 *
 * @author YangWanLi
 * @date 2018/7/9 16:29
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Permission {

    String[] value() default {};

    String[] marks() default {};
}
