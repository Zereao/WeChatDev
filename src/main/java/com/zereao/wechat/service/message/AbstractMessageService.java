package com.zereao.wechat.service.message;

import com.zereao.wechat.dao.MessageDAO;
import com.zereao.wechat.data.bo.Message;
import com.zereao.wechat.data.vo.MessageVO;
import com.zereao.wechat.service.factory.AbstractMsgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zereao
 * @version 2018/12/11  18:20
 */
public abstract class AbstractMessageService extends AbstractMsgService {
    @Autowired
    private MessageDAO messageDAO;

    /**
     * 处理消息，如果需要自动被动回复，则将返回内容返回
     *
     * @param msgVO 需要处理的 MessageVO 实体
     * @return 返回消息(如果有)，否则应该返回 "success" 或者 ""(空字符串)
     */
    public abstract Object handleMessage(MessageVO msgVO);

    @Override
    public Object handleMsg(MessageVO msg) {
        Message message = new Message();
        BeanUtils.copyProperties(msg, message);
        messageDAO.save(message);
        return this.handleMessage(msg);
    }
}
