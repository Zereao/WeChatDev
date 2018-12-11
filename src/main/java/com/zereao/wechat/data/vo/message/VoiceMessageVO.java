package com.zereao.wechat.data.vo.message;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 语音消息
 *
 * @author Zereao
 * @version 2018/12/11  15:20
 */
@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class VoiceMessageVO {
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
     * 消息类型，语音为voice
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    /**
     * 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    @XmlElement(name = "MediaId")
    private String mediaId;
    /**
     * 语音格式，如amr，speex等
     */
    @XmlElement(name = "Format")
    private String format;
    /**
     * 语音识别结果，UTF8编码
     */
    @XmlElement(name = "Recognition")
    private String recognition;
    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private String msgId;
}
