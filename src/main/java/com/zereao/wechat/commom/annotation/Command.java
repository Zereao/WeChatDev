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
     * 菜单等级，Level.L1 一级菜单，Level.L2 二级菜单  | Level.L0 通配菜单
     */
    Level level();

    /**
     * 菜单类型，Root菜单 或 用户菜单
     */
    MenuType menu() default MenuType.USER;

    /**
     * 目标资源类型，默认文本
     */
    TargetSource src() default TargetSource.TEXT;

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
         * 通配菜单，一定是最后一级菜单
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

    enum TargetSource {
        /**
         * 文本
         */
        TEXT,
        /**
         * 图片
         */
        IMAGE
    }
}
