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
    boolean first() default false;

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
}
