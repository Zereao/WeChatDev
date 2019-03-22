package com.zereao.wechat.common.aspects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * @author Darion Mograine H
 * @version 2018/12/11  17:49
 */
@Slf4j
@ControllerAdvice("com.zereao.wechat.controller")
public class GlobalExceptionHandler {
    private static final String ERROR = "ERROR";

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error("出现未知异常！错误信息：", e);
        return ERROR;
    }

    @ResponseBody
    @ExceptionHandler(ReflectiveOperationException.class)
    public String handleReflectiveOperationException(ReflectiveOperationException e) {
        log.error("Bean的转换出现异常！", e);
        return ERROR;
    }

    @ResponseBody
    @ExceptionHandler(ParseException.class)
    public String handleParseException(ParseException e) {
        log.error("时间处理异常！", e);
        return ERROR;
    }
}
