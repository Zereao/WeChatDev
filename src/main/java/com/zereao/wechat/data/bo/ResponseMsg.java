package com.zereao.wechat.data.bo;

import com.zereao.wechat.commom.constant.MsgType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * 回复的消息，存在MongoDB中
 *
 * @author Zereao
 * @version 2018/12/18  10:53
 */
@Data
@Document("response_msg")
public class ResponseMsg {
    /**
     * 主键ID
     */
    @Id
    private Integer id;
    /**
     * 接收方帐号（收到的OpenID）
     */
    private String toUserName;
    /**
     * 开发者微信号
     */
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private Date createTime;
    /**
     * 消息类型
     */
    private MsgType msgType;
    /**
     * 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    private String content;
    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    private String mediaId;
    /**
     * 视频消息的标题
     */
    private String title;
    /**
     * 视频消息的描述
     */
    private String description;
    /**
     * 音乐链接
     */
    private String musicUrl;
    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
     */
    private String hqMusicUrl;
    /**
     * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String thumbMediaId;
    /**
     * 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private Integer articleCount;
    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String picUrl;
    /**
     * 点击图文消息跳转链接
     */
    private String url;
}
