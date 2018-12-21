package com.zereao.wechat.service.command;

import com.google.common.base.CaseFormat;
import com.zereao.wechat.commom.constant.Command;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.message.ConstantMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Zereao
 * @version 2018/12/19  20:21
 */
@Slf4j
@Service
public abstract class AbstractCommandService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    private ConstantMessageService constantMessageService;

    /**
     * 执行命令
     *
     * @param command 命令
     * @param msgVO   封装了参数的消息实体
     * @return 返回值
     */
    public Object exec(Command command, ParentMsgVO msgVO) {
        log.info("------->  准备执行 command = {}", command);
        /* 方法名，必须和命令的名称 格式转换后相同 */
        String methodName = StringUtils.uncapitalize(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, command.name()));
        Class<?> curClass = this.getClass();
        String methodFullName = curClass.getName().concat(".").concat(methodName);
        String toUserName = msgVO.getFromUserName();
        try {
            Method targetMethod = curClass.getMethod(methodName, ParentMsgVO.class);
            return targetMethod.invoke(this, msgVO);
        } catch (NoSuchMethodException e) {
            log.warn("方法 {} 不存在，将返回帮助信息", methodFullName);
            return constantMessageService.getHelp(toUserName);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("执行方法 {} 失败！\n{}", methodFullName, e);
            return constantMessageService.getErrorMsg(toUserName);
        }
    }
}
