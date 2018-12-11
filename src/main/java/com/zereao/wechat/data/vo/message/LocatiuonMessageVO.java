package com.zereao.wechat.data.vo.message;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 地理位置消息
 *
 * @author Zereao
 * @version 2018/12/11  15:21
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocatiuonMessageVO {
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
     * 消息类型，location
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 地理位置纬度
     */
    @XmlElement(name = "Location_X")
    private String locationX;
    /**
     * 地理位置经度
     */
    @XmlElement(name = "Location_Y")
    private String locationY;
    /**
     * 地图缩放大小
     */
    @XmlElement(name = "Scale")
    private String scale;
    /**
     * 地理位置信息
     */
    @XmlElement(name = "Label")
    private String label;
    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private String msgId;
}
