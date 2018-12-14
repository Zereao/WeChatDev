package com.zereao.wechat.commom.constant;

/**
 * @author Zereao
 * @version 2018/12/14  17:10
 */
public enum ReturnCode {
    /**
     * 成功
     */
    SUCCESS("SUCCESS"),
    /**
     * 失败
     */
    ERROR("ERROR");

    ReturnCode(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return this.value;
    }
}
