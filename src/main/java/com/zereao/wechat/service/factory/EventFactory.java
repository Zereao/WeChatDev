package com.zereao.wechat.service.factory;

import com.google.common.base.CaseFormat;
import com.zereao.wechat.commom.constant.Event;
import com.zereao.wechat.data.dto.VoiceMessageDTO;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.event.AbstractEventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 事件工厂
 *
 * @author Zereao
 * @version 2018/12/13  14:49
 */
@Service
public class EventFactory {
    private final Map<String, AbstractEventService> eventServiceMap;

    @Autowired
    public EventFactory(Map<String, AbstractEventService> eventServiceMap) {this.eventServiceMap = eventServiceMap;}

    public AbstractMsgService getInstance(ParentMsgVO parentVo) {
        String eventName;
        switch (parentVo.getEvent()) {
            case SUBSCRIBE:
            case UNSUBSCRIBE:
                eventName = Event.SUBSCRIBE.name();
                break;
            default:
                eventName = parentVo.getEvent().name();
        }
        String beanName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, eventName).concat("EventService");
        return eventServiceMap.get(StringUtils.uncapitalize(beanName));
    }
}
