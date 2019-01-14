package com.zereao.wechat.service.factory;

import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.event.AbstractEventService;
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

    public AbstractMsgService getInstance(MessageVO msgVO) {
        return eventServiceMap.get(msgVO.getEvent().value().concat("EventService"));
    }
}
