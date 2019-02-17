package com.zereao.wechat.common.annotation;

import java.lang.annotation.*;

/**
 * @author Darion Mograine H
 * @version 2019/02/17  13:26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Operate {
    /**
     * 菜单映射
     */
    String value();

    /**
     * 操作类型，USER 用户操作， ROOT ROOT操作
     */
    OperateType type() default OperateType.USER;


    enum OperateType {
        /**
         * 用户操作
         */
        USER,
        /**
         * ROOT操作
         */
        ROOT
    }
}
