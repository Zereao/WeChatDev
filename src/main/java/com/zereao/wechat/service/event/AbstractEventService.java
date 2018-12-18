package com.zereao.wechat.service.event;

import com.zereao.wechat.dao.UserDAO;
import com.zereao.wechat.data.bo.User;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.factory.AbstractMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zereao
 * @version 2018/12/13  15:11
 */
@Service
public abstract class AbstractEventService extends AbstractMsgService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    private UserDAO userDAO;

    /**
     * 处理 事件消息，假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
     *
     * @param parentVO 事件消息实体
     * @return 返回值
     */
    public abstract Object handleEvent(ParentMsgVO parentVO);

    @Override
    public Object handleMsg(ParentMsgVO msg) {
        switch (msg.getEvent()) {
            case SUBSCRIBE:
                User user = User.builder().deleteFlag(0).openid(msg.getFromUserName()).build();
                userDAO.save(user);
                break;
            default:
                break;
        }
        return this.handleEvent(msg);
    }
}
