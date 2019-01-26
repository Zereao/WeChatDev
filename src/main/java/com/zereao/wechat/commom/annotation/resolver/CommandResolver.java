package com.zereao.wechat.commom.annotation.resolver;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.utils.PackageUtils;
import com.zereao.wechat.commom.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Method;

/**
 * @author Darion Mograine H
 * @version 2019/01/14  14:39
 */
@Slf4j
public class CommandResolver implements Runnable {
    private Class cls;

    private CommandResolver(Class cls) {
        this.cls = cls;
    }

    public static void run(Class cls) {
        log.info("开始扫描 Command ......");
        ThreadPoolUtils.execute(new CommandResolver(cls));
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
                        log.debug("Put the command into the CommandHolder ------->  mapping = {}, name = {}, menu = {},bean = {}, class = {}, method = {}",
                                command.mapping(), command.name(), command.menu().name(), StringUtils.uncapitalize(clz.getSimpleName()), clz.getName(), method.getName());
                    }
                }
            });
        }
        log.info("Command 扫描完毕！CommandHolder = {}", CommandsHolder.values());
    }

}
