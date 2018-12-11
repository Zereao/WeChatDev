package com.zereao.wechat.commom.constant;

/**
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
    LINK("link");

    private String type;

    MsgType(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }
}
