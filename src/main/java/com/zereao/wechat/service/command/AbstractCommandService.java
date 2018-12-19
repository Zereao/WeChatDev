package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.constant.Command;
import com.zereao.wechat.data.vo.ParentMsgVO;

/**
 * @author Zereao
 * @version 2018/12/19  20:21
 */
public abstract class AbstractCommandService {
    public abstract Object exec(Command command, ParentMsgVO msgVO);
}
