package com.zereao.wechat.data.vo.message;

import com.zereao.wechat.commom.constant.MsgType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 文本消息
 *
 * @author Zereao
 * @version 2018/12/11  15:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TextMessageVO extends AbstractMessageVO {
    /**
     * 接收方微信号
     */
    private String toUserName;
    /**
     * 发送方微信号，若为普通用户，则是一个OpenID
     */
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private Date createTime;
    /**
     * 消息类型，text
     */
    private MsgType msgType;
    /**
     * 文本消息内容
     */
    private String content;
    /**
     * 消息id，64位整型
     */
    private Long msgId;
}
