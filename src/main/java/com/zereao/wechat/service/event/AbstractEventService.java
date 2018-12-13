package com.zereao.wechat.service.event;

import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.factory.AbstractMsgService;

/**
 * @author Zereao
 * @version 2018/12/13  15:11
 */
public abstract class AbstractEventService extends AbstractMsgService {
    /**
     * 处理 事件消息，假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
     *
     * @param parentVO 事件消息实体
     * @return 返回值
     */
    public abstract String handleEvent(ParentMsgVO parentVO);

    @Override
    public String handleMsg(ParentMsgVO msg) {
        return handleEvent(msg);
    }
}
