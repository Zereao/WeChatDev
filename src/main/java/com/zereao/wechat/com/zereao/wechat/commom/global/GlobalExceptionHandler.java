package com.zereao.wechat.com.zereao.wechat.commom.global;

import com.zereao.wechat.com.zereao.wechat.commom.constant.ResultCode;
import com.zereao.wechat.com.zereao.wechat.commom.constant.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 *
 * @author Zereao
 * @version 2018/11/03  21:25
 */
@ControllerAdvice(basePackages = {"com.zereao.wechat.controller"})
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResultModel handleException(Exception e) {
        logger.error(" ---------->  Error!", e);
        return new ResultModel(ResultCode.ERROR);
    }

}
