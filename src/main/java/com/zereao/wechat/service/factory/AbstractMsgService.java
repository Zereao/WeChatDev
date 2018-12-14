package com.zereao.wechat.service.factory;


import com.zereao.wechat.data.vo.ParentMsgVO;

/**
 * 抽象顶级消息处理类
 *
 * @author Zereao
 * @version 2018/12/13  14:52
 */
public abstract class AbstractMsgService {
    /**
     * 处理消息，并按需做出返回
     *
     * @param parentMsgVO 包含所有的参数的 MsgVO
     * @return 返回体
     */
    public abstract Object handleMsg(ParentMsgVO parentMsgVO);
}
