package com.zereao.wechat.common.constant;

import java.util.Arrays;

/**
 * 事件类型枚举
 *
 * @author Darion Mograine H
 * @version 2018/12/12  11:20
 */
public enum Event {
    /**
     * 订阅
     */
    SUBSCRIBE("subscribe"),
    /**
     * 取消订阅
     */
    UNSUBSCRIBE("unsubscribe"),
    /**
     * 用户已关注时，扫描带参数二维码事件
     */
    SCAN("SCAN"),
    /**
     * 上报地理位置事件
     */
    LOCATION("LOCATION"),
    /**
     * 自定义菜单事件，点击菜单拉取消息时的事件推送
     */
    CLICK("CLICK"),
    /**
     * 自定义菜单事件，点击菜单跳转链接时的事件推送
     */
    VIEW("VIEW");

    private String type;

    Event(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }

    public static Event of(String type) {
        return Arrays.stream(Event.values()).filter(e -> e.value().equalsIgnoreCase(type)).findFirst().orElse(null);
    }
}
