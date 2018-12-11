package com.zereao.wechat.data.vo.message;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 文本消息
 *
 * @author Zereao
 * @version 2018/12/11  15:16
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class TextMessageVO {
    /**
     * 接收方微信号
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 发送方微信号，若为普通用户，则是一个OpenID
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XmlElement(name = "CreateTime")
    private String createTime;
    /**
     * 消息类型，text
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 文本消息内容
     */
    @XmlElement(name = "Content")
    private String content;
    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private String msgId;
}
