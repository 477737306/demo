package com.cmit.testing.common.annotion;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author YangWanLi
 * @date 2018/8/7 16:29
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    String[] value() default {};
}
