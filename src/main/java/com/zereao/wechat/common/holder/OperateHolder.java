package com.zereao.wechat.common.holder;

import com.zereao.wechat.common.annotation.Operate.OperateType;
import com.zereao.wechat.common.utils.SpringBeanUtils;
import com.zereao.wechat.service.redis.RedisService;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Darion Mograine H
 * @version 2019/02/17  13:33
 */
@Component
public class OperateHolder {
    private static Map<String, Operate> holder = new ConcurrentHashMap<>(16);

    /**
     * 是否启用ROOT 权限 redis key前缀
     */
    private static final String ROOT_ENABLED_PREFIX = "ROOT_ENABLE_OF_";

    /**
     * 向容器中添加一条命令
     *
     * @param operate {@link com.zereao.wechat.common.annotation.Operate}，包含信息的 Operate 注解对象
     * @param cls     Operate 所在类 的 Class对象
     * @param method  Operate 所标注方法的Method对象
     */
    public static void add(com.zereao.wechat.common.annotation.Operate operate, Class<?> cls, Method method) {
        String mapping = operate.value();
        holder.put(mapping, Operate.builder().mapping(mapping).bean(StringUtils.uncapitalize(cls.getSimpleName()))
                .type(operate.type()).cls(cls).method(method).build());
    }

    /**
     * 获取操作
     *
     * @param mapping 操作对应的命令映射
     * @return 命令
     */
    public static Operate get(String mapping) {
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
     * 获取容器中是否包含当前映射的操作
     *
     * @param mapping 操作对应的命令映射
     * @return true OR false
     */
    public static boolean contains(String mapping) {
        return holder.containsKey(mapping);
    }

    /**
     * 获取所有 操作，按照 命令映射(mapping) 排序
     * <p>
     * key = 命令名称，value = 命令映射mapping
     *
     * @param openid 用户的openid，用来确认当前用户是否是ROOT用户
     * @return result LinkedHashMap
     */
    public static List<String> list(String openid) {
        List<String> opList = new ArrayList<>();
        OperateType type = "true".equals(SpringBeanUtils.getBean(RedisService.class)
                .get(ROOT_ENABLED_PREFIX + openid)) ? OperateType.ROOT : OperateType.USER;
        holder.entrySet().stream()
                // 剔除被 @Deprecated 标注的Command
                .filter(entry -> entry.getValue().cls.getAnnotation(Deprecated.class) == null)
                .filter(entry -> type.equals(OperateType.ROOT) || entry.getValue().type.equals(type))
                .sorted(Comparator.comparing(Map.Entry::getKey)).forEach(entry -> opList.add(entry.getKey()));
        return opList;
    }

    /*
     * 根据菜单类型 和 扫描类Class对象获取 Class下所有符合条件的菜单命令，按照 命令映射(mapping) 排序
     * <p>
     * key = 命令名称，value = 命令映射mapping
     *
     * @param openid 当前用户的openid
     * @param cls    扫描类的Class对象
     * @param level  菜单等级，1级菜单，2级菜单
     * @return result LinkedHashMap

    public static Map<String, String> list(String openid, Class cls, Level level) {
        menu = "true".equals(redisService.get(ROOT_ENABLED_PREFIX + openid)) ? MenuType.ROOT : MenuType.USER;
        Map<String, String> resultMap = new LinkedHashMap<>();
        holder.entrySet().stream()
                .filter(entry -> {
                    Command command = entry.getValue();
                    return command.cls == cls && command.level == level && (menu.equals(MenuType.ROOT) || command.menu.equals(menu));
                })
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> resultMap.put(entry.getValue().name, entry.getKey()));
        return resultMap;
    }*/

    /**
     * 自定义的 toString()
     */
    public static String values() {
        return "[ OperateHolder.size() = " + holder.size() + ", Content =[" + holder + "] ]";
    }

    @Builder
    public static class Operate {
        public String mapping, bean;
        public OperateType type;
        public Class<?> cls;
        public Method method;

        @Override
        public String toString() {
            return "Operate{" +
                    "mapping='" + mapping + '\'' +
                    ", bean='" + bean + '\'' +
                    ", type='" + type + '\'' +
                    ", cls=" + (cls == null ? null : StringUtils.uncapitalize(cls.getSimpleName())) +
                    ", method=" + (method == null ? null : method.getName()) +
                    "}";
        }
    }
}
