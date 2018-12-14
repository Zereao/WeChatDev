package com.zereao.wechat.commom.aspects;

import com.zereao.wechat.commom.constant.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * @author Zereao
 * @version 2018/12/11  17:49
 */
@Slf4j
@ControllerAdvice("com.zereao.wechat.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception e) {
        log.error("出现未知异常！错误信息：", e);
        return ReturnCode.ERROR.value();
    }

    @ExceptionHandler(ReflectiveOperationException.class)
    @ResponseBody
    public String handleReflectiveOperationException(ReflectiveOperationException e) {
        log.error("Bean的转换出现异常！", e);
        return ReturnCode.ERROR.value();
    }

    @ExceptionHandler(ParseException.class)
    @ResponseBody
    public String handleParseException(ParseException e) {
        log.error("时间处理异常！", e);
        return ReturnCode.ERROR.value();
    }
}
