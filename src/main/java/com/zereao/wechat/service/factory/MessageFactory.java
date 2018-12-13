package com.zereao.wechat.service.factory;

import com.google.common.base.CaseFormat;
import com.zereao.wechat.data.vo.ParentMessageVO;
import com.zereao.wechat.service.message.AbstractMessageService;
import org.apache.commons.lang3.StringUtils;
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

    public AbstractMsgService getInstance(ParentMessageVO parentVo) {
        String beanName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, parentVo.getMsgType().name()).concat("MessageService");
        return messageServiceMap.get(StringUtils.uncapitalize(beanName));
    }
}
