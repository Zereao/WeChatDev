package com.zereao.wechat.commom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Darion Mograine H
 * @version 2019/01/14  11:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name() default "";

    String[] value() default {};

    String[] path() default {};
}
