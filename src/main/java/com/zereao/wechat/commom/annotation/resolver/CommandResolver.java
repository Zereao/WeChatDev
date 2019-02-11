package com.zereao.wechat.commom.annotation.resolver;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.utils.PackageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Method;

/**
 * {@link Command}注解的解析器，用来处理所有 @Command 标注的命令
 * <p>
 * 其实际功能是，扫描{@link com.zereao.wechat.WechatApplication}类上注解{@link SpringBootApplication}中 scanBasePackages 值所标识的包中所有类中被{@link Command} 标识的方法的相关信息
 *
 * @author Darion Mograine H
 * @version 2019/01/14  14:39
 */
@Slf4j
public class CommandResolver implements Runnable {
    private Class cls;

    public CommandResolver(Class cls) {
        this.cls = cls;
    }

    @Override
    public void run() {
        SpringBootApplication annotation = (SpringBootApplication) this.cls.getAnnotation(SpringBootApplication.class);
        String[] scanBasePackages = annotation.scanBasePackages();
        for (String basePackage : scanBasePackages) {
            PackageUtils.getClass(basePackage, true).forEach(clz -> {
                for (Method method : clz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Command.class)) {
                        Command command = method.getAnnotation(Command.class);
                        CommandsHolder.add(command, clz, method);
                        log.info("Put the command into the CommandHolder ------->  mapping = {}, name = {}, first = {}, menu = {}, bean = {}, class = {}, method = {}",
                                command.mapping(), command.name(), command.menu().name(), command.first(), StringUtils.uncapitalize(clz.getSimpleName()), clz.getName(), method.getName());
                    }
                }
            });
        }
        log.info("Command 扫描完毕！CommandHolder = {}", CommandsHolder.values());
    }
}
