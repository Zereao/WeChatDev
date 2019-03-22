# message 表 建表语句
CREATE TABLE `message`
(
  `id`             INT(11) NOT NULL AUTO_INCREMENT,
  `to_user_name`   VARCHAR(50)  DEFAULT NULL COMMENT '本条消息收件人的openid，即公众号对应的openid',
  `from_user_name` VARCHAR(50)  DEFAULT NULL COMMENT '发件人的openid',
  `create_time`    DATETIME     DEFAULT NULL COMMENT '消息创建时间',
  `msg_id`         BIGINT(64)      DEFAULT NULL COMMENT '消息ID，64位整型',
  `msg_type`       VARCHAR(10)  DEFAULT NULL COMMENT '消息类型',
  `content`        TEXT         DEFAULT NULL COMMENT '消息内容',
  `media_id`       VARCHAR(255)  DEFAULT NULL COMMENT '多媒体消息媒体ID，可以调用多媒体文件下载接口拉取数据。比如：图片消息、视频消息等',
  `pic_url`        VARCHAR(255) DEFAULT NULL COMMENT '图片链接（由系统生成）',
  `location_x`     FLOAT(12, 7) DEFAULT NULL COMMENT '地理位置纬度',
  `location_y`     FLOAT(12, 7) DEFAULT NULL COMMENT '地理位置经度',
  `scale`          FLOAT(12, 7) DEFAULT NULL COMMENT '地图缩放大小',
  `label`          VARCHAR(50)  DEFAULT NULL COMMENT '地理位置信息',
  `thumb_media_id` VARCHAR(50)  DEFAULT NULL COMMENT '视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据',
  `format`         VARCHAR(10)  DEFAULT NULL COMMENT '语音格式：amr',
  `recognition`    VARCHAR(255) DEFAULT NULL COMMENT '语音识别结果，UTF8编码',
  `event`          VARCHAR(10)  DEFAULT NULL COMMENT '事件类型',
  `event_key`      VARCHAR(50)  DEFAULT NULL COMMENT '事件KEY值',
  `ticker`         VARCHAR(10)  DEFAULT NULL COMMENT '二维码的ticket，可用来换取二维码图片',
  `latitude`       FLOAT(12, 7) DEFAULT NULL COMMENT '地理位置纬度',
  `longitude`      FLOAT(12, 7) DEFAULT NULL COMMENT '地理位置经度',
  `precision`      FLOAT(12, 7) DEFAULT NULL COMMENT '精度',
  PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;

# USER表建表语句
CREATE TABLE `user`
(
  `id`          INT(11) NOT NULL AUTO_INCREMENT,
  `username`    VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `openid`      VARCHAR(50) DEFAULT NULL COMMENT '用户的openid',
  `delete_flag` INT(1)      DEFAULT 0 COMMENT '删除标识，1 - 已删除；0 - 未删除',
  `create_time` DATETIME    DEFAULT NULL COMMENT '创建时间',
  `update_time` DATETIME    DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4;
