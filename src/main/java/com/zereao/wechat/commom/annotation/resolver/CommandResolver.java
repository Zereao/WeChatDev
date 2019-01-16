package com.zereao.wechat.commom.annotation.resolver;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.utils.PackageUtils;
import com.zereao.wechat.commom.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
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
        SpringBootApplication annotation = (SpringBootApplication) cls.getAnnotation(SpringBootApplication.class);
        String[] scanBasePackages = annotation.scanBasePackages();
        for (String basePackage : scanBasePackages) {
            PackageUtils.getClass(basePackage, true).forEach(clz -> {
                if (clz.isAnnotationPresent(Command.class)) {
                    Command classCommand = (Command) clz.getAnnotation(Command.class);
                    String classCommandName = classCommand.mapping();
                    CommandsHolder.add(classCommandName, classCommand.name(), classCommand.level(), clz);
                    log.debug("Put the command into the CommandHolder ------->  command = {}, name = {}", classCommandName, clz.getName());
                    for (Method method : clz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Command.class)) {
                            Command command = method.getAnnotation(Command.class);
                            String childCommand = classCommandName.concat("-").concat(command.mapping());
                            CommandsHolder.add(childCommand, command.name(), command.level(), null);
                            log.debug("Put the command into the CommandHolder ------->  command = {}, name = {}", childCommand, method.getName());
                        }
                    }
                }
            });
        }
        log.info("Command 扫描完毕！CommandHolder = {}", CommandsHolder.values());
    }

}
