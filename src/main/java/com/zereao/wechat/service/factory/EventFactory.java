package com.zereao.wechat.service.factory;

import com.google.common.base.CaseFormat;
import com.zereao.wechat.data.vo.ParentMessageVO;
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

    public AbstractMsgService getInstance(ParentMessageVO parentVo) {
        String beanName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, parentVo.getEvent().name()).concat("EventService");
        return eventServiceMap.get(StringUtils.uncapitalize(beanName));
    }
}
