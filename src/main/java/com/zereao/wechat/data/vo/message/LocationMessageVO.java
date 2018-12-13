package com.zereao.wechat.data.vo.message;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.vo.AbstractMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 地理位置消息
 *
 * @author Zereao
 * @version 2018/12/11  15:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LocationMessageVO extends AbstractMsg {
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
     * 消息类型，location
     */
    private MsgType msgType;
    /**
     * 地理位置纬度
     */
    private Double locationX;
    /**
     * 地理位置经度
     */
    private Double locationY;
    /**
     * 地图缩放大小
     */
    private Double scale;
    /**
     * 地理位置信息
     */
    private String label;
    /**
     * 消息id，64位整型
     */
    private Long msgId;
}
