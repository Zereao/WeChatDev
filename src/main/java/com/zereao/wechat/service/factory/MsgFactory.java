package com.zereao.wechat.service.factory;

import com.zereao.wechat.common.constant.Event;
import com.zereao.wechat.common.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.event.AbstractEventService;
import com.zereao.wechat.service.message.AbstractMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 工厂类
 *
 * @author Darion Mograine H
 * @version 2019/01/14  14:10
 */
@Service
public class MsgFactory {

    private final Map<String, AbstractMessageService> messageServiceMap;
    private final Map<String, AbstractEventService> eventServiceMap;

    @Autowired
    public MsgFactory(Map<String, AbstractMessageService> messageServiceMap, Map<String, AbstractEventService> eventServiceMap) {
        this.messageServiceMap = messageServiceMap;
        this.eventServiceMap = eventServiceMap;
    }

    /**
     * 根据消息体内容，获取相应的Service
     *
     * @param msgVO 消息VO
     * @return AbstractMsgService
     */
    public AbstractMsgService getInstance(MessageVO msgVO) {
        MsgType msgType = msgVO.getMsgType();
        return MsgType.TEXT.equals(msgType) ? this.getMessageService(msgType) : this.getEventService(msgVO.getEvent());
    }

    /**
     * 根据消息类型，获取对应的消息处理Service
     *
     * @param msgType 消息类型
     * @return ? extends AbstractMessageService
     */
    private AbstractMessageService getMessageService(MsgType msgType) {
        return messageServiceMap.get((msgType.equals(MsgType.SHORT_VIDEO) ? MsgType.VIDEO : msgType).value() + "MessageService");
    }

    /**
     * 根据事件类型，获取对应的事件处理Service
     *
     * @param event 事件类型
     * @return ? extends AbstractEventService
     */
    private AbstractEventService getEventService(Event event) {
        return eventServiceMap.get((event.equals(Event.UNSUBSCRIBE) ? Event.SUBSCRIBE : event).value() + "EventService");
    }
}
