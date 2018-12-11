package com.zereao.wechat.data.vo.message;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 链接消息
 *
 * @author Zereao
 * @version 2018/12/11  15:21
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class LinkMessageVO {
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
     * 消息创建时间
     */
    @XmlElement(name = "CreateTime")
    private String createTime;
    /**
     * 消息类型，link
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 消息标题
     */
    @XmlElement(name = "Title")
    private String title;
    /**
     * 消息描述
     */
    @XmlElement(name = "Description")
    private String description;
    /**
     * 消息链接
     */
    @XmlElement(name = "Url")
    private String url;
    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private String msgId;
}
