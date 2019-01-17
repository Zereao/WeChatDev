package com.zereao.wechat.commom.annotation.resolver;

import lombok.Builder;

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
        holder.put(mapping, Command.builder().mapping(mapping).cls(cls).method(method).name(command.name()).menu(command.menu()).build());
    }

    /**
     * 获取命令
     *
     * @param mapping 命令映射
     * @return 命令
     */
    public static Command getCommand(String mapping) {
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
     * 获取所有 一级菜单命令，按照 命令映射(mapping) 排序
     * <p>
     * key = 命令名称，value = 命令映射mapping
     *
     * @return result LinkedHashMap
     */
    public static Map<String, String> list() {
        Map<String, String> resultMap = new LinkedHashMap<>();
        holder.entrySet().stream().filter(entry -> entry.getValue().menu).sorted(Comparator.comparing(Map.Entry::getKey)).forEach(entry -> resultMap.put(entry.getValue().name, entry.getKey()));
        return resultMap;
    }

    /**
     * 自定义的 toString()
     */
    static String values() {
        StringBuilder sb = new StringBuilder("[ ClassHolder.size() = ").append(holder.size());
        sb.append(", Content =");
        holder.forEach((k, command) -> sb
                .append(" {name=").append(command.name)
                .append(", mapping=").append(command.mapping)
                .append(", menu=").append(command.menu)
                .append(", class=").append(command.cls == null ? null : command.cls.getName())
                .append(", method=").append(command.method == null ? null : command.method.getName()).append("},"));
        sb.append("]");
        return sb.toString();
    }

    @Builder
    public static class Command {
        private String mapping, name;
        private boolean menu;
        private Class cls;
        private Method method;
    }
}
