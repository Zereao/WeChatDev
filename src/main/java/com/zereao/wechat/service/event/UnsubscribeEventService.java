package com.zereao.wechat.service.event;

import com.zereao.wechat.dao.UserDAO;
import com.zereao.wechat.data.bo.User;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.data.vo.event.SubscribeEventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 关注/取消关注事件
 *
 * @author Zereao
 * @version 2018/12/13  15:12
 */
@Slf4j
@Service
public class UnsubscribeEventService extends AbstractEventService {
    private final UserDAO userDAO;

    @Autowired
    public UnsubscribeEventService(UserDAO userDAO) {this.userDAO = userDAO;}

    @Override
    public Object handleEvent(ParentMsgVO parentVO) {
        // 订阅和取消订阅时，数据体一样，只是Event属性的值不一样 unsubscribe / subscribe
        SubscribeEventVO eventVO = new SubscribeEventVO();
        BeanUtils.copyProperties(parentVO, eventVO);
        String openId = eventVO.getFromUserName();
        User user = userDAO.findUserByOpenid(openId);
        user.setDeleteFlag(1);
        userDAO.save(user);
        return "SUCCESS";
    }
}
