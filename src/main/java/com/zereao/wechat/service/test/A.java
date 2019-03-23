package com.zereao.wechat.service.test;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @author Darion Mograine H
 * @version 2019/03/23  15:32
 */
public class A {
    public static void main(String[] args) throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resourcePatternResolver.getResources("classpath*:com/zereao/wechat/**/*.class");
        for (Resource resource : resources) {
            System.out.println(resource.getURI());
        }
    }
}
