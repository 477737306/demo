package com.cmit.testing.common.annotion;

import java.lang.annotation.*;

/**
 * 脚本执行注解
 * Created by Suviky on 2018/7/30 11:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface ScriptExecute {
}
