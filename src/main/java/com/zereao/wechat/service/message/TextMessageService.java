package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.commom.constant.Command;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.command.AbstractCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 处理接收到的文本消息的Service
 *
 * @author Zereao
 * @version 2018/12/11  20:03
 */
@Slf4j
@Service
public class TextMessageService extends AbstractMessageService {
    private final Map<String, AbstractCommandService> commandServiceMap;

    @Autowired
    public TextMessageService(Map<String, AbstractCommandService> commandServiceMap) {this.commandServiceMap = commandServiceMap;}

    @Override
    public Object handleMessage(MessageVO msgVO) {
//        CommandsHolder.get
        String openid = msgVO.getFromUserName();
        return null;
    }
}
