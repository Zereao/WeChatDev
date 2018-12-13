package com.zereao.wechat.service.message;

import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.factory.AbstractMsgService;

/**
 * @author Zereao
 * @version 2018/12/11  18:20
 */
public abstract class AbstractMessageService extends AbstractMsgService {
    /**
     * 处理消息，如果需要自动被动回复，则将返回内容返回
     *
     * @param parentVO 需要处理的 MessageVO 实体
     * @return 返回消息(如果有)，否则应该返回 "success" 或者 ""(空字符串)
     */
    public abstract String handleMessage(ParentMsgVO parentVO);

    @Override
    public String handleMsg(ParentMsgVO msg) {
        return this.handleMessage(msg);
    }
}
