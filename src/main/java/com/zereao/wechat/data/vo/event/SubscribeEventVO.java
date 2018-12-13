package com.zereao.wechat.data.vo.event;

import com.zereao.wechat.commom.constant.Event;
import com.zereao.wechat.commom.constant.MsgType;
import lombok.Data;

import java.util.Date;

/**
 * 关注/取消关注事件
 *
 * @author Zereao
 * @version 2018/12/11  15:16
 */
@Data
public class SubscribeEventVO {
    /**
     * 接收方微信号
     */
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private Date createTime;
    /**
     * 消息类型，event
     */
    private MsgType msgType;
    /**
     * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)
     */
    private Event event;
}
