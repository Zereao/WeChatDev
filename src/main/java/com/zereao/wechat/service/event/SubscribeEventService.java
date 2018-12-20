package com.zereao.wechat.service.event;

import com.zereao.wechat.dao.UserDAO;
import com.zereao.wechat.data.bo.User;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.message.ConstantMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 关注事件
 *
 * @author Zereao
 * @version 2018/12/13  15:12
 */
@Slf4j
@Service
public class SubscribeEventService extends AbstractEventService {
    private final UserDAO userDAO;
    private final ConstantMessageService constantMessageService;


    @Autowired
    public SubscribeEventService(UserDAO userDAO, ConstantMessageService constantMessageService) {
        this.userDAO = userDAO;
        this.constantMessageService = constantMessageService;
    }

    @Override
    public Object handleEvent(ParentMsgVO eventVO) {
        String fromUser = eventVO.getFromUserName();
        User user = User.builder().createTime(new Date()).deleteFlag(0).openid(fromUser).build();
        userDAO.save(user);
        return constantMessageService.getWelcomeArticle(fromUser);
    }
}
