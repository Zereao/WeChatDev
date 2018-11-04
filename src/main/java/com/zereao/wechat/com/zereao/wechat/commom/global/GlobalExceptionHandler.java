package com.zereao.wechat.com.zereao.wechat.commom.global;

import com.zereao.wechat.com.zereao.wechat.commom.constant.ResultCode;
import com.zereao.wechat.com.zereao.wechat.commom.constant.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 *
 * @author Zereao
 * @version 2018/11/03  21:25
 */
@Slf4j
@ControllerAdvice(basePackages = {"com.zereao.wechat.controller"})
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultModel handleException(Exception e) {
        log.error(" ---------->  Error!", e);
        return new ResultModel(ResultCode.ERROR);
    }

}
