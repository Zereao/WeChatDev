package com.zereao.wechat.data.vo.message;

import com.zereao.wechat.commom.constant.MsgType;
import lombok.Data;

import java.util.Date;

/**
 * 语音消息
 *
 * @author Zereao
 * @version 2018/12/11  15:20
 */
@Data
public class VoiceMessageVO {
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
     * 消息类型，语音为voice
     */
    private MsgType msgType;
    /**
     * 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    private String mediaId;
    /**
     * 语音格式，如amr，speex等
     */
    private String format;
    /**
     * 语音识别结果，UTF8编码
     */
    private String recognition;
    /**
     * 消息id，64位整型
     */
    private Long msgId;
}
