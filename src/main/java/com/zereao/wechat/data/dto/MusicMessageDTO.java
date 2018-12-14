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
public class MusicMessageDTO {
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
     * 回复消息类型，music
     */
    @XmlElement(name = "MsgType")
    @XmlJavaTypeAdapter(JaxbMsgTypeAdapter.class)
    private MsgType msgType;
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    @XmlElement(name = "MediaId")
    private String mediaId;

    @XmlElement(name = "Music")
    private Music music;

    @Data
    @Builder
    @XmlRootElement(name = "Music")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Music {
        /**
         * 音乐标题，非 必需
         */
        @XmlElement(name = "Title")
        private String title;
        /**
         * 音乐描述，非 必需
         */
        @XmlElement(name = "Description")
        private String description;
        /**
         * 音乐链接，非 必需
         */
        @XmlElement(name = "MusicURL")
        private String musicURL;
        /**
         * 高质量音乐链接，WIFI环境优先使用该链接播放音乐，非 必需
         */
        @XmlElement(name = "HQMusicUrl")
        private String hqMusicUrl;
        /**
         * 视缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
         */
        @XmlElement(name = "ThumbMediaId")
        private String thumbMediaId;
    }
}
