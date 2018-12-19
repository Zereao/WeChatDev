package com.zereao.wechat.service.event;

import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.welcome.WelcomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 关注事件
 *
 * @author Zereao
 * @version 2018/12/13  15:12
 */
@Slf4j
@Service
public class SubscribeEventService extends AbstractEventService {
    private final WelcomeService welcomeService;

    @Autowired
    public SubscribeEventService(WelcomeService welcomeService) {this.welcomeService = welcomeService;}

    @Override
    public Object handleEvent(ParentMsgVO parentVO) {
        return welcomeService.getWelcomeArticle(parentVO.getFromUserName());
    }
}
