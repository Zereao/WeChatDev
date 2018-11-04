package com.zereao.wechat.commom.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zereao
 * @version 2018/11/04  22:32
 */
@Slf4j
@Aspect
public class GlobalLogPrintAspects {
    @Before("execution(* com.zereao.wechat.controller..*.*(..))")
    public void beforeLogPrinter(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        String method = signature.getName();
        String[] parameterNames = ((MethodSignature) signature).getParameterNames();

        ConcurrentHashMap<String, String> paramMap = null;

        if (Objects.nonNull(parameterNames)) {
            paramMap = new ConcurrentHashMap<>(16);
            for (int i = 0, j = parameterNames.length; i < j; i++) {
                String parameterName = parameterNames[i];
                String value = args[i] == null ? "null" : args[i].toString();
                paramMap.put(parameterName, value);
            }
        }
        log.info("--------> method = {}, parameters = {}", method, paramMap);
    }

}
