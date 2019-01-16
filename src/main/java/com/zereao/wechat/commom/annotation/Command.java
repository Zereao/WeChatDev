package com.zereao.wechat.commom.annotation;

import java.lang.annotation.*;

/**
 * @author Darion Mograine H
 * @version 2019/01/14  11:54
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Command {
    /**
     * 命令名称
     */
    String name() default "";

    /**
     * 菜单映射
     */
    String mapping();

    /**
     * 菜单等级，一级菜单/二级菜单  1/2
     */
    int level() default 0;
}
