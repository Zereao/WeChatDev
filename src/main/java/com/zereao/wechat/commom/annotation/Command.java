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
     * 是否一级菜单，默认 false
     */
    Level level() default Level.L0;

    /**
     * 菜单类型，Root菜单 或 用户菜单
     */
    Command.MenuType menu() default MenuType.USER;

    enum MenuType {
        /**
         * Root权限
         */
        ROOT,
        /**
         * 用户权限
         */
        USER
    }

    enum Level {
        /**
         * 零级菜单，通配
         */
        L0,
        /**
         * 一级菜单
         */
        L1,
        /**
         * 二级菜单
         */
        L2
    }
}
