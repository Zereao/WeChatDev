package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.message.HelpMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Darion Mograine H
 * @version 2018/12/19  20:21
 */
@Slf4j
public abstract class AbstractCommandService {
    @Autowired
    private HelpMessageService helpMessageService;

    /**
     * 执行命令
     *
     * @param msgVO 封装了参数的消息实体
     * @return 返回值
     */
    public Object exec(MessageVO msgVO, CommandsHolder.Command command) {
        Class<?> curClass = this.getClass();
        String className = curClass.getName();
        log.info("------->  {}准备执行命令 {}", className, command.toString());
        try {
            return command.method.invoke(this, msgVO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("执行方法 {} 失败！\n{}", command.method.getName(), e);
            return helpMessageService.getErrorMsg(msgVO.getFromUserName());
        }
    }
}
