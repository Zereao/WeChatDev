package com.zereao.wechat.service.event;

import com.zereao.wechat.dao.UserDAO;
import com.zereao.wechat.pojo.po.User;
import com.zereao.wechat.pojo.vo.MessageVO;
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

    @Autowired
    public SubscribeEventService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Object handleEvent(MessageVO eventVO) {
        String fromUser = eventVO.getFromUserName();
        User user = User.builder().createTime(new Date()).deleteFlag(0).openid(fromUser).build();
        userDAO.save(user);
        return helpMessageService.getWelcomeArticle(eventVO);
    }
}
