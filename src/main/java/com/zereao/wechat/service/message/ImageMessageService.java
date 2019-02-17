package com.zereao.wechat.service.message;

import com.zereao.wechat.common.annotation.Command.TargetSource;
import com.zereao.wechat.common.holder.CommandsHolder;
import com.zereao.wechat.common.holder.CommandsHolder.Command;
import com.zereao.wechat.common.holder.OperateHolder;
import com.zereao.wechat.common.holder.OperateHolder.Operate;
import com.zereao.wechat.pojo.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Darion Mograine H
 * @version 2019/01/16  18:31
 */
@Slf4j
@Service
public class ImageMessageService extends AbstractMessageService {

    @Override
    public Object handleMessage(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        // 当前 命令树中的命令
        Command curCmd = CommandsHolder.get(redisService.get(COMMAND_TEEE_PREFIX + openid));
        boolean imgReady = "true".equals(redisService.get(IMG_READY_PREFIX + openid)) && curCmd.src.equals(TargetSource.IMAGE);
        if (!imgReady) {
            return helpMessageService.getImgReadyErrorMsg(openid);
        }
        Operate operate = OperateHolder.get(curCmd.mapping);
        try {
            log.info("------->  {}准备执行命令 {}", operate.bean, operate.toString());
            return operate.method.invoke(operate.cls, msgVO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("{}命令执行失败！\n", operate.toString(), e);
            return helpMessageService.getErrorMsg(openid);
        }
    }
}
