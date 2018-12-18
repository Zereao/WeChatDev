package com.zereao.wechat.service.message;

import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.data.vo.message.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author Zereao
 * @version 2018/12/11  20:03
 */
@Slf4j
@Service
public class TextMessageService extends AbstractMessageService {

    @Override
    public Object handleMessage(ParentMsgVO parentVO) {
        TextMessageVO messageVO = new TextMessageVO();
        BeanUtils.copyProperties(parentVO, messageVO);



        return "SUCCESS";
    }
}
