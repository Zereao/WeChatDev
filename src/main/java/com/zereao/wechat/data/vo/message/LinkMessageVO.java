package com.zereao.wechat.data.vo.message;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.vo.AbstractMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 链接消息
 *
 * @author Zereao
 * @version 2018/12/11  15:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkMessageVO extends AbstractMsg {
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
     * 消息类型，link
     */
    private MsgType msgType;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息描述
     */
    private String description;
    /**
     * 消息链接
     */
    private String url;
    /**
     * 消息id，64位整型
     */
    private Long msgId;
}
