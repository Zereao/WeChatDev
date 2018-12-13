package com.zereao.wechat.data.vo.event;

import com.zereao.wechat.commom.constant.Event;
import com.zereao.wechat.commom.constant.MsgType;
import lombok.Data;

import java.util.Date;

/**
 * 上报地理位置事件
 *
 * @author Zereao
 * @version 2018/12/11  15:21
 */
@Data
public class LocationEventVO {
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
     * 消息类型，event
     */
    private MsgType msgType;
    /**
     * 事件类型，LOCATION
     */
    private Event event;
    /**
     * 地理位置纬度
     */
    private Double latitude;
    /**
     * 地理位置经度
     */
    private Double longitude;
    /**
     * 地理位置精度
     */
    private Double precision;
}
