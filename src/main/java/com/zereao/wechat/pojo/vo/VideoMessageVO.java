package com.zereao.wechat.pojo.vo;

import com.zereao.wechat.common.constant.MsgType;
import com.zereao.wechat.common.utils.jaxbadapter.JaxbDateAdapter;
import com.zereao.wechat.common.utils.jaxbadapter.JaxbMsgTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoMessageVO {
    /**
     * 接收方帐号（收到的OpenID）
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 开发者微信号
     */
    @Builder.Default
    @XmlElement(name = "FromUserName")
    private String fromUserName = "gh_eeaaf5c9001c";
    /**
     * 消息创建时间 （整型）
     */
    @Builder.Default
    @XmlElement(name = "CreateTime")
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date createTime = new Date();
    /**
     * 回复消息类型，video
     */
    @Builder.Default
    @XmlElement(name = "MsgType")
    @XmlJavaTypeAdapter(JaxbMsgTypeAdapter.class)
    private MsgType msgType = MsgType.VIDEO;

    @XmlElement(name = "Video")
    private Video video;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement(name = "Video")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Video {
        /**
         * 通过素材管理中的接口上传多媒体文件，得到的id。
         */
        @XmlElement(name = "MediaId")
        private String mediaId;
        /**
         * 视频消息的标题，非 必需
         */
        @XmlElement(name = "Title")
        private String title;
        /**
         * 视频消息的描述，非 必需
         */
        @XmlElement(name = "Description")
        private String description;
    }
}
