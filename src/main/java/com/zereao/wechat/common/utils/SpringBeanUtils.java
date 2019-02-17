package com.zereao.wechat.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Darion Mograine H
 * @version 2019/02/17  21:17
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    /**
     * 根据bean的Class 对象获取bean
     *
     * @param tCls bean的 Class 对象
     * @param <T>  泛型
     * @return bean
     */
    public static <T> T getBean(Class<T> tCls) {
        return applicationContext.getBean(tCls);
    }
}
