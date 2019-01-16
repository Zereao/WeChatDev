package com.zereao.wechat.commom.annotation.resolver;

import lombok.Builder;

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
     * @param name    命令名称
     * @param cls     @Command标注的类或Method的Class对象
     */
    public static void add(String command, String name, int level, Class cls) {
        holder.put(command, Command.builder().command(command).cls(cls).name(name).level(level).build());
    }

    public static String getName(String command) {
        return holder.get(command).name;
    }

    public static Class getClass(String command) {
        return holder.get(command).cls;
    }

    public static int size() {
        return holder.size();
    }

    public static boolean contains(String command) {
        return holder.containsKey(command);
    }

    /**
     * 获取所有 level级 命令，按照 命令(command) 排序
     * <p>
     * key = 命令名称，value = 命令 command
     *
     * @return result LinkedHashMap
     */
    public static Map<String, String> list(int level) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        holder.entrySet().stream().filter(entry -> entry.getValue().level == level).sorted(Comparator.comparing(entry -> Integer.valueOf(entry.getKey()))).forEach(entry -> resultMap.put(entry.getValue().name, entry.getKey()));
        return resultMap;
    }

    public static String values() {
        StringBuilder sb = new StringBuilder("[ ClassHolder.size() = ").append(holder.size());
        sb.append(", Content = {");
        holder.forEach((k, command) -> sb.append(k).append(":")
                .append(" {name=").append(command.name)
                .append(", command=").append(command.command)
                .append(", level=").append(command.level)
                .append(", class=").append(command.cls == null ? null : command.cls.getName()).append("}, "));
        sb.append("} ]");
        return sb.toString();
    }

    @Builder
    private static class Command {
        private String command, name;
        private int level;
        private Class cls;
    }
}
