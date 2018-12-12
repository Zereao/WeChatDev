package com.zereao.wechat.service.message;

import com.google.common.base.CaseFormat;
import com.zereao.wechat.commom.constant.MsgType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Zereao
 * @version 2018/12/11  18:17
 */
@Service
public class MessageFactory {

    private final Map<String, AbstractMessageService> messageServiceMap;

    @Autowired
    public MessageFactory(Map<String, AbstractMessageService> messageServiceMap) {this.messageServiceMap = messageServiceMap;}

    public AbstractMessageService getInstance(MsgType msgType) {
        String beanName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, msgType.name()).concat("MessageService");
        return messageServiceMap.get(beanName);
    }
}
