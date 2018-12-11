package com.zereao.wechat.commom.aspects;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author Zereao
 * @version 2018/11/04  22:32
 */
@Slf4j
@Aspect
@Component
public class GlobalLogPrintAspects {
    @Before("execution(* com.zereao.wechat.controller..*.*(..))")
    public void beforeLogPrinter(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String method = joinPoint.getSignature().getName();
        log.info("--------> method = {}(), parameters = {}", method, JSONObject.toJSON(args));
    }

    @AfterReturning(returning = "result", pointcut = "execution(* com.zereao.wechat.controller..*.*(..))")
    public void afterReturnLogPrinter(Object result) {
        log.info("--------> result = {}", result);
    }
}
