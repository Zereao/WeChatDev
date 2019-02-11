package com.zereao.wechat.commom.annotation.resolver;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Darion Mograine H
 * @version 2019/01/16  10:21
 */
public class CommandsHolder {
    private static Map<String, Command> holder = new ConcurrentHashMap<>(16);

    /**
     * 向容器中添加一条命令
     *
     * @param command 命令
     */
    static void add(com.zereao.wechat.commom.annotation.Command command, Class cls, Method method) {
        String mapping = command.mapping();
        holder.put(mapping, Command.builder().mapping(mapping).first(command.first()).bean(StringUtils.uncapitalize(cls.getSimpleName())).cls(cls).method(method).name(command.name()).menu(command.menu()).build());
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
     * @param menu  菜单类型，Root菜单 或者 用户菜单
     * @param first 是否一级菜单
     * @return result LinkedHashMap
     */
    public static Map<String, String> list(com.zereao.wechat.commom.annotation.Command.MenuType menu, boolean first) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        holder.entrySet().stream()
                .filter(entry -> (entry.getValue().first == first && (menu.equals(com.zereao.wechat.commom.annotation.Command.MenuType.ROOT) || entry.getValue().menu.equals(menu))))
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> resultMap.put(entry.getValue().name, entry.getKey()));
        return resultMap;
    }

    /**
     * 自定义的 toString()
     */
    static String values() {
        StringBuilder sb = new StringBuilder("[ ClassHolder.size() = ").append(holder.size());
        sb.append(", Content =[").append(holder).append("]");
        return sb.toString();
    }

    @Builder
    public static class Command {
        public String mapping, name, bean;
        public boolean first;
        public com.zereao.wechat.commom.annotation.Command.MenuType menu;
        public Class cls;
        public Method method;

        @Override
        public String toString() {
            return "Command{" +
                    "mapping='" + mapping + '\'' +
                    ", name='" + name + '\'' +
                    ", bean='" + bean + '\'' +
                    ", first='" + first + '\'' +
                    ", menu=" + menu.name() +
                    ", cls=" + (cls == null ? null : StringUtils.uncapitalize(cls.getSimpleName())) +
                    ", method=" + (method == null ? null : method.getName()) +
                    "}";
        }
    }
}
