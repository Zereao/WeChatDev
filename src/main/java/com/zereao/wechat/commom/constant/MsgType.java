package com.zereao.wechat.commom.constant;

/**
 * 消息类型枚举
 *
 * @author Zereao
 * @version 2018/12/11  19:01
 */
public enum MsgType {
    /**
     * 文本消息
     */
    TEXT("text"),
    /**
     * 图片消息
     */
    IMAGE("image"),
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

    MsgType(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }

    public static MsgType of(String type) {

    }
}
