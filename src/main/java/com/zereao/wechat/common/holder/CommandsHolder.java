package com.zereao.wechat.common.holder;

import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.annotation.Command.MenuType;
import com.zereao.wechat.common.annotation.Command.TargetSource;
import com.zereao.wechat.common.utils.SpringBeanUtils;
import com.zereao.wechat.service.redis.RedisService;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Darion Mograine H
 * @version 2019/01/16  10:21
 */
@Component
public class CommandsHolder {
    private static Map<String, Command> holder = new ConcurrentHashMap<>(16);

    // 是否启用ROOT 权限 redis key前缀
    private static final String ROOT_ENABLED_PREFIX = "ROOT_ENABLE_OF_";

    /**
     * 向容器中添加一条命令
     *
     * @param command {@link com.zereao.wechat.common.annotation.Command}，包含信息的 Command 注解对象
     * @param cls     Command 所在类 的 Class对象
     * @param method  Command 所标注方法的Method对象
     */
    public static void add(com.zereao.wechat.common.annotation.Command command, Class cls, Method method) {
        String mapping = command.mapping();
        holder.put(mapping, Command.builder().mapping(mapping).level(command.level()).src(command.src()).bean(StringUtils.uncapitalize(cls.getSimpleName())).cls(cls).method(method).name(command.name()).menu(command.menu()).build());
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

    /**
     * 获取容器中是否包含当前命令
     *
     * @param mapping 命令映射
     * @return true OR false
     */
    public static boolean contains(String mapping) {
        return holder.containsKey(mapping);
    }

    /**
     * 根据菜单类型获取所有 菜单命令，按照 命令映射(mapping) 排序
     * <p>
     * key = 命令名称，value = 命令映射mapping
     *
     * @param openid 用户的openid，用来确认当前用户是否是ROOT用户
     * @param level  菜单等级，1级菜单，2级菜单
     * @return result LinkedHashMap
     */
    public static Map<String, String> list(String openid, Level level) {
        MenuType menu = "true".equals(SpringBeanUtils.getBean(RedisService.class).get(ROOT_ENABLED_PREFIX + openid)) ? MenuType.ROOT : MenuType.USER;
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
        MenuType menu = "true".equals(SpringBeanUtils.getBean(RedisService.class).get(ROOT_ENABLED_PREFIX + openid)) ? MenuType.ROOT : MenuType.USER;
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
    public static String values() {
        StringBuilder sb = new StringBuilder("[ CommandsHolder.size() = ").append(holder.size());
        sb.append(", Content =[").append(holder).append("] ]");
        return sb.toString();
    }

    @Builder
    public static class Command {
        public String mapping, name, bean;
        public Level level;
        public MenuType menu;
        public TargetSource src;
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
                    ", src=" + src.name() +
                    ", cls=" + (cls == null ? null : StringUtils.uncapitalize(cls.getSimpleName())) +
                    ", method=" + (method == null ? null : method.getName()) +
                    "}";
        }
    }
}
