package com.zereao.wechat.pojo.vo;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.jaxbadapter.JaxbDateAdapter;
import com.zereao.wechat.commom.utils.jaxbadapter.JaxbMsgTypeAdapter;
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
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class NewsMessageVO {
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

    public NewsMessageVO() { }

    public NewsMessageVO(String toUserName, String fromUserName, Date createTime, MsgType msgType, Integer articleCount, Articles articles) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
        this.msgType = msgType;
        this.articleCount = articleCount;
        this.articles = articles;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public Articles getArticles() {
        return articles;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement(name = "Articles")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Articles {
        @XmlElement(name = "item")
        private Item item;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
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

    public static NewMessageVOBuilder builder() {
        return new NewMessageVOBuilder();
    }

    public static class NewMessageVOBuilder {
        private String toUserName;
        private String fromUserName;
        private Date createTime;
        private MsgType msgType;
        private Integer articleCount;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public NewMessageVOBuilder toUserName(String toUserName) {
            this.toUserName = toUserName;
            return this;
        }

        public NewMessageVOBuilder fromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
            return this;
        }

        public NewMessageVOBuilder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public NewMessageVOBuilder msgType(MsgType msgType) {
            this.msgType = msgType;
            return this;
        }

        public NewMessageVOBuilder articleCount(Integer articleCount) {
            this.articleCount = articleCount;
            return this;
        }

        public NewMessageVOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NewMessageVOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public NewMessageVOBuilder picUrl(String picUrl) {
            this.picUrl = picUrl;
            return this;
        }

        public NewMessageVOBuilder url(String url) {
            this.url = url;
            return this;
        }

        public NewsMessageVO build() {
            NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title(this.title).picUrl(this.picUrl)
                    .url(this.url).description(this.description).build();
            NewsMessageVO.Articles articles = NewsMessageVO.Articles.builder().item(item).build();
            return new NewsMessageVO(toUserName, fromUserName, createTime, msgType, articleCount, articles);
        }
    }
}
