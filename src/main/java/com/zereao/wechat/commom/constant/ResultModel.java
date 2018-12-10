package com.zereao.wechat.commom.constant;

import lombok.Data;

/**
 * @author Zereao
 * @version 2018/11/03  18:15
 */
@Data
public class ResultModel<T> {
    private String code;
    private String message;
    private T data;

    public ResultModel(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultModel(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResultModel(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}
