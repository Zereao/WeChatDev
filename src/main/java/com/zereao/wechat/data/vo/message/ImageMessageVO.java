package com.zereao.wechat.data.vo.message;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.vo.AbstractMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 图片消息
 *
 * @author Zereao
 * @version 2018/12/11  15:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImageMessageVO extends AbstractMsg {
    /**
     * 接收方微信号
     */
    private String toUserName;
    /**
     * 发送方微信号，若为普通用户，则是一个OpenID
     */
    private String fromUserName;
    /**
     * 消息创建时间
     */
    private Date createTime;
    /**
     * 消息类型，image
     */
    private MsgType msgType;
    /**
     * 图片链接（由系统生成）
     */
    private String picUrl;
    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    private String mediaId;
    /**
     * 消息id，64位整型
     */
    private Long msgId;
}
