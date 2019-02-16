package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.Command.TargetSource;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.pojo.vo.MessageVO;
import org.springframework.stereotype.Service;

/**
 * @author Darion Mograine H
 * @version 2019/01/16  18:31
 */
@Service
public class ImageMessageService extends AbstractMessageService {

    @Override
    public Object handleMessage(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        if (!this.imgReady(openid)) {
            return helpMessageService.getImgReadyErrorMsg(openid);
        }
        //
        return null;
    }

    /**
     * 检查当前用户是否在图片等待状态，并且 当前命令树中命令的TargetSource 是 TargetSource.IMAGE
     *
     * @param openid 当前用户的openid
     * @return true or false ；用户是否是在图片等待状态
     */
    private boolean imgReady(String openid) {
        return "true".equals(redisService.get(IMG_READY_PREFIX + openid)) &&
                CommandsHolder.get(redisService.get(COMMAND_TEEE_PREFIX + openid)).src.equals(TargetSource.IMAGE);
    }
}
