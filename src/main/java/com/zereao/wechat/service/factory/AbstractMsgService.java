package com.zereao.wechat.service.factory;

import com.zereao.wechat.data.vo.AbstractMsg;

/**
 * 抽象顶级消息处理类
 *
 * @author Zereao
 * @version 2018/12/13  14:52
 */
public abstract class AbstractMsgService {
    public abstract String handleMsg(AbstractMsg msg);
}
