package com.zereao.wechat.service.message;

import com.zereao.wechat.data.vo.AbstractMsg;
import com.zereao.wechat.data.vo.message.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Zereao
 * @version 2018/12/11  20:03
 */
@Slf4j
@Service
public class TextMessageService extends AbstractMessageService {

    @Override
    public String handleMessage(AbstractMsg absMessageVO) {
        log.info("AbstractMsg = {}", absMessageVO);
        TextMessageVO messageVO = (TextMessageVO) absMessageVO;
        log.info("SubscribeEventVO = {}", messageVO);
        return "SUCCESS";
    }
}