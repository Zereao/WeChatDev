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
public class NewsMessageDTO {
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
     * 回复消息类型，news
     */
    @XmlElement(name = "MsgType")
    @XmlJavaTypeAdapter(JaxbMsgTypeAdapter.class)
    private MsgType msgType;
    /**
     * 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    @XmlElement(name = "ArticleCount")
    private Integer articleCount;
    /**
     * 图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数
     */
    @XmlElement(name = "Articles")
    private Articles articles;

    @Data
    @Builder
    @XmlRootElement(name = "Articles")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Articles {
        @XmlElement(name = "item")
        private Item item;

        @Data
        @Builder
        @XmlRootElement(name = "item")
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Item {
            /**
             * 图文消息标题
             */
            @XmlElement(name = "Title")
            private String title;
            /**
             * 图文消息描述
             */
            @XmlElement(name = "Description")
            private String description;
            /**
             * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
             */
            @XmlElement(name = "PicUrl")
            private String picUrl;
            /**
             * 点击图文消息跳转链接
             */
            @XmlElement(name = "Url")
            private String url;
        }
    }
}
