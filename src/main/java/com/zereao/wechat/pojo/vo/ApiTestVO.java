package com.zereao.wechat.pojo.vo;

import lombok.Data;

/**
 * @author Zereao
 * @version 2018/11/03  19:15
 */
@Data
public class ApiTestVO {
    /**
     * 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     */
    private String signature;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 随机数
     */
    private String nonce;
    /**
     * 随机字符串
     */
    private String echostr;
}
