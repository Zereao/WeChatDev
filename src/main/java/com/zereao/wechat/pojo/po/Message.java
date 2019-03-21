package com.zereao.wechat.pojo.po;

import com.zereao.wechat.common.constant.Event;
import com.zereao.wechat.common.constant.MsgType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 消息实体
 *
 * @author Zereao
 * @version 2018/11/03  21:18
 */
@Data
@Entity
@Table(name = "message")
public class Message {
    /**
     * ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 开发者微信号
     */
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserName;
    /**
     * 消息创建时间
     */
    private Date createTime;
    /**
     * 消息id，64位整型
     */
    private Long msgId;
    /**
     * 消息类型
     */
    @Enumerated(EnumType.STRING)
    private MsgType msgType;
    /**
     * 文本消息内容
     */
    private String content;
    /**
     * 多媒体消息媒体id，可以调用多媒体文件下载接口拉取数据。比如：图片消息、视频消息等
     */
    private String mediaId;
    /**
     * 图片链接（由系统生成）
     */
    private String picUrl;
    /**
     * 地理位置纬度
     */
    @Column(name = "location_x")
    private Double locationX;
    /**
     * 地理位置经度
     */
    @Column(name = "location_y")
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
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private String thumbMediaId;
    /**
     * 语音格式：amr
     */
    private String format;
    /**
     * 语音识别结果，UTF8编码
     */
    private String recognition;
    /**
     * 事件类型
     */
    @Enumerated(EnumType.STRING)
    private Event event;
    /**
     * 事件KEY值
     */
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    private String ticket;
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
    @Column(name = "[precision]")
    private Double precision;
}
