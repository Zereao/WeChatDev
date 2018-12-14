package com.zereao.wechat.data.dto;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.jaxbadapter.JaxbDateAdapter;
import com.zereao.wechat.commom.utils.jaxbadapter.JaxbMsgTypeAdapter;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * 回复文本消息
 *
 * @author Zereao
 * @version 2018/12/13  18:44
 */
@Data
@Builder
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoiceMessageDTO {
    /**
     * 接收方帐号（收到的OpenID）
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 开发者微信号
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XmlElement(name = "CreateTime")
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date createTime;
    /**
     * 回复消息类型，text
     */
    @XmlElement(name = "MsgType")
    @XmlJavaTypeAdapter(JaxbMsgTypeAdapter.class)
    private MsgType msgType;

    @XmlElement(name = "Voice")
    private Voice voice;

    @Data
    @XmlRootElement(name = "Voice")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Voice {
        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id。
         */
        @XmlElement(name = "MediaId")
        private String mediaId;
    }
}