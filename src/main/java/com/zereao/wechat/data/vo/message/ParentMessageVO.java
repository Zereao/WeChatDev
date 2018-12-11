package com.zereao.wechat.data.vo.message;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.jaxbadapter.JaxbDateAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

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
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date createTime;
    @XmlElement(name = "MsgId")
    private Long msgId;
    @XmlElement(name = "MsgType")
    private MsgType msgType;
    @XmlElement(name = "Content")
    private String content;
    @XmlElement(name = "MediaId")
    private String mediaId;
    @XmlElement(name = "PicUrl")
    private String picUrl;
    @XmlElement(name = "Location_X")
    private Double locationX;
    @XmlElement(name = "Location_Y")
    private Double locationY;
    @XmlElement(name = "Scale")
    private Double scale;
    @XmlElement(name = "Label")
    private String label;
    @XmlElement(name = "ThumbMediaId")
    private String thumbMediaId;
    @XmlElement(name = "Format")
    private String format;
    @XmlElement(name = "Recognition")
    private String recognition;
}
