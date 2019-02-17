package com.zereao.wechat.common.annotation.resolver;

import com.zereao.wechat.common.annotation.Operate;
import com.zereao.wechat.common.holder.OperateHolder;
import com.zereao.wechat.common.utils.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Method;

/**
 * @author Darion Mograine H
 * @version 2019/02/17  13:30
 */
@Slf4j
public class OperateResolver implements Runnable {
    private Class cls;

    public OperateResolver(Class cls) {
        this.cls = cls;
    }

    @Override
    public void run() {
        SpringBootApplication annotation = (SpringBootApplication) this.cls.getAnnotation(SpringBootApplication.class);
        String[] scanBasePackages = annotation.scanBasePackages();
        for (String basePackage : scanBasePackages) {
            PackageUtils.getClass(basePackage, true).forEach(clz -> {
                for (Method method : clz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Operate.class)) {
                        Operate operate = method.getAnnotation(Operate.class);
                        OperateHolder.add(operate, clz, method);
                        log.info("Put the operate into the OperateHolder ------->  mapping = {}, bean = {}, type = {}, class = {}, method = {}",
                                operate.value(), StringUtils.uncapitalize(clz.getSimpleName()), operate.type().name(), clz.getName(), method.getName());
                    }
                }
            });
        }
        log.info("Operate 扫描完毕！OperateHolder = {}", OperateHolder.values());
    }
}
