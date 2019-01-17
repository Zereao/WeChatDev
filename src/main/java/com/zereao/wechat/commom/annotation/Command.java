package com.zereao.wechat.commom.annotation;

import java.lang.annotation.*;

/**
 * @author Darion Mograine H
 * @version 2019/01/14  11:54
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Command {
    /**
     * 菜单映射
     */
    String mapping();

    /**
     * 命令名称
     */
    String name() default "";

    /**
     * 是否 一级菜单
     */
    boolean menu() default false;
}
