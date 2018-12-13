package com.zereao.wechat.service.event;

import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.data.vo.event.SubscribeEventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 关注/取消关注事件
 *
 * @author Zereao
 * @version 2018/12/13  15:12
 */
@Slf4j
@Service
public class SubscribeEventService extends AbstractEventService {
    @Override
    public String handleEvent(ParentMsgVO parentVO) {
        SubscribeEventVO eventVO = new SubscribeEventVO();
        BeanUtils.copyProperties(parentVO, eventVO);
        
        return "SUCCESS";
    }
}
