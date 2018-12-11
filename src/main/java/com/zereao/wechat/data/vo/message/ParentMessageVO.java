package com.zereao.wechat.data.vo.message;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 父级MessageVO，可以接收到所有消息类型的参数
 *
 * @author Zereao
 * @version 2018/12/11  15:16
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParentMessageVO {
    @XmlElement(name = "ToUserName")
    private String toUserName;
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    @XmlElement(name = "CreateTime")
    private String createTime;
    @XmlElement(name = "MsgId")
    private String msgId;
    @XmlElement(name = "MsgType")
    private String msgType;

    @XmlElement(name = "Content")
    private String content;
    @XmlElement(name = "MediaId")
    private String mediaId;
    @XmlElement(name = "PicUrl")
    private String picUrl;
    @XmlElement(name = "Location_X")
    private String locationX;
    @XmlElement(name = "Location_Y")
    private String locationY;
    @XmlElement(name = "Scale")
    private String scale;
    @XmlElement(name = "Label")
    private String label;
    @XmlElement(name = "ThumbMediaId")
    private String thumbMediaId;
    @XmlElement(name = "Format")
    private String format;
    @XmlElement(name = "Recognition")
    private String recognition;

}
