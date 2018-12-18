package com.zereao.wechat.service.factory;

import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.message.AbstractMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 消息工厂
 *
 * @author Zereao
 * @version 2018/12/11  18:17
 */
@Service
public class MessageFactory {

    private final Map<String, AbstractMessageService> messageServiceMap;

    @Autowired
    public MessageFactory(Map<String, AbstractMessageService> messageServiceMap) {this.messageServiceMap = messageServiceMap;}

    public AbstractMsgService getInstance(ParentMsgVO parentVo) {
        return messageServiceMap.get(parentVo.getMsgType().value().concat("MessageService"));
    }
}
