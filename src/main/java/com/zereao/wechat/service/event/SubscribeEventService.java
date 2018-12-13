package com.zereao.wechat.service.event;

import com.zereao.wechat.data.vo.AbstractMsg;
import org.springframework.stereotype.Service;

/**
 * @author Zereao
 * @version 2018/12/13  15:12
 */
@Service
public class SubscribeEventService extends AbstractEventService {
    @Override
    public String handleEvent(AbstractMsg eventVO) {
        return null;
    }
}
