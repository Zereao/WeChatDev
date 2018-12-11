package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.vo.message.AbstractMessageVO;
import org.springframework.stereotype.Service;

/**
 * @author Zereao
 * @version 2018/12/11  20:03
 */
@Service
public class TextMessageService extends AbstractMessageService {

    @Override
    public String handleMsg(AbstractMessageVO messageVO) {
        return null;
    }
}
