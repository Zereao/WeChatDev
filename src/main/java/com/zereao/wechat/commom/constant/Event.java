package com.zereao.wechat.commom.constant;

/**
 * 事件类型枚举
 *
 * @author Zereao
 * @version 2018/12/12  11:20
 */
public enum Event {
    /**
     * 文本消息
     */
    SUBSCRIBE("subscribe"),
    /**
     * 图片消息
     */
    UNSUBSCRIBE("unsubscribe"),
    /**
     * 语音消息
     */
    VOICE("voice"),
    /**
     * 视频消息
     */
    VIDEO("video"),
    /**
     * 小视频消息
     */
    SHORT_VIDEO("shortvideo"),
    /**
     * 地理位置消息
     */
    LOCATION("location"),
    /**
     * 链接消息
     */
    LINK("link"),
    /**
     * 事件
     */
    EVENT("event");

    private String type;

    Event(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }
}
