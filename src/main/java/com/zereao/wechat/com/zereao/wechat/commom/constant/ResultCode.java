package com.zereao.wechat.com.zereao.wechat.commom.constant;

/**
 * @author Zereao
 * @version 2018/11/03  18:16
 */
public enum ResultCode {
    /**
     * 全局返回，成功
     */
    SUCCESS("0000", "处理成功！"),
    /**
     * 全局返回，失败
     */
    ERROR("0009", "处理失败");

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
