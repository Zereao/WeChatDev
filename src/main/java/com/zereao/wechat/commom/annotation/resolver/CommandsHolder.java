package com.zereao.wechat.commom.annotation.resolver;

import com.zereao.wechat.commom.annotation.Command.Level;
import com.zereao.wechat.commom.annotation.Command.MenuType;
import com.zereao.wechat.service.redis.RedisService;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zereao.wechat.service.message.AbstractMessageService.ROOT_ENABLED_PREFIX;

/**
 * @author Darion Mograine H
 * @version 2019/01/16  10:21
 */
@Component
public class CommandsHolder implements ApplicationContextAware {
    private static Map<String, Command> holder = new ConcurrentHashMap<>(16);

    private static RedisService redisService;

    /**
     * 向容器中添加一条命令
     *
     * @param command 命令
     */
    static void add(com.zereao.wechat.commom.annotation.Command command, Class cls, Method method) {
        String mapping = command.mapping();
        holder.put(mapping, Command.builder().mapping(mapping).level(command.level()).bean(StringUtils.uncapitalize(cls.getSimpleName())).cls(cls).method(method).name(command.name()).menu(command.menu()).build());
    }

    /**
     * 获取命令
     *
     * @param mapping 命令映射
     * @return 命令
     */
    public static Command get(String mapping) {
        return holder.get(mapping);
    }

    /**
     * 获取容器中命令的数目
     *
     * @return 容器中命令的数目
     */
    public static int size() {
        return holder.size();
    }

    public static boolean contains(String mapping) {
        return holder.containsKey(mapping);
    }

    /**
     * 根据菜单类型获取所有 菜单命令，按照 命令映射(mapping) 排序
     * <p>
     * key = 命令名称，value = 命令映射mapping
     *
     * @param level 菜单等级，1级菜单，2级菜单
     * @return result LinkedHashMap
     */
    public static Map<String, String> list(String openid, Level level) {
        MenuType menu = "true".equals(redisService.get(ROOT_ENABLED_PREFIX + openid)) ? MenuType.ROOT : MenuType.USER;
        Map<String, String> resultMap = new LinkedHashMap<>();
        holder.entrySet().stream()
                .filter(entry -> {
                    Command command = entry.getValue();
                    return command.level == level && (menu.equals(MenuType.ROOT) || command.menu.equals(menu));
                })
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> resultMap.put(entry.getValue().name, entry.getKey()));
        return resultMap;
    }

    /**
     * 根据菜单类型 和 扫描类Class对象获取 Class下所有符合条件的菜单命令，按照 命令映射(mapping) 排序
     * <p>
     * key = 命令名称，value = 命令映射mapping
     *
     * @param openid 当前用户的openid
     * @param cls    扫描类的Class对象
     * @param level  菜单等级，1级菜单，2级菜单
     * @return result LinkedHashMap
     */
    public static Map<String, String> list(String openid, Class cls, Level level) {
        MenuType menu = "true".equals(redisService.get(ROOT_ENABLED_PREFIX + openid)) ? MenuType.ROOT : MenuType.USER;
        Map<String, String> resultMap = new LinkedHashMap<>();
        holder.entrySet().stream()
                .filter(entry -> {
                    Command command = entry.getValue();
                    return command.cls == cls && command.level == level && (menu.equals(MenuType.ROOT) || command.menu.equals(menu));
                })
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> resultMap.put(entry.getValue().name, entry.getKey()));
        return resultMap;
    }

    /**
     * 自定义的 toString()
     */
    static String values() {
        StringBuilder sb = new StringBuilder("[ ClassHolder.size() = ").append(holder.size());
        sb.append(", Content =[").append(holder).append("] ]");
        return sb.toString();
    }

    @Builder
    public static class Command {
        public String mapping, name, bean;
        public Level level;
        public MenuType menu;
        public Class cls;
        public Method method;

        @Override
        public String toString() {
            return "Command{" +
                    "mapping='" + mapping + '\'' +
                    ", name='" + name + '\'' +
                    ", bean='" + bean + '\'' +
                    ", level='" + level + '\'' +
                    ", menu=" + menu.name() +
                    ", cls=" + (cls == null ? null : StringUtils.uncapitalize(cls.getSimpleName())) +
                    ", method=" + (method == null ? null : method.getName()) +
                    "}";
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        redisService = applicationContext.getBean(RedisService.class);
    }
}
