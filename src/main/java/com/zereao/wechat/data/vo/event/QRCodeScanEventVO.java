package com.zereao.wechat.data.vo.event;

import com.zereao.wechat.commom.constant.Event;
import com.zereao.wechat.commom.constant.MsgType;
import lombok.Data;

import java.util.Date;

/**
 * 扫描带参数二维码事件
 *
 * @author Zereao
 * @version 2018/12/11  15:20
 */
@Data
public class QRCodeScanEventVO {
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
     * 事件类型，subscribe 或者 SCAN
     */
    private Event event;
    /**
     * 事件KEY值，qrscene_为前缀，后面为二维码的参数值 / 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
     */
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String ticket;
}
