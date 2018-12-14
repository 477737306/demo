package com.cmit.testing.common.annotion;

import java.lang.annotation.*;

/**
 * 外部接口鉴权注解
 *
 * @author YangWanLi
 * @date 2018/7/9 16:29
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface ForeignPermission {

    String[] value() default {};
}
