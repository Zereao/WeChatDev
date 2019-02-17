package com.zereao.wechat.service.message;

import com.zereao.wechat.dao.MessageDAO;
import com.zereao.wechat.pojo.po.Message;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.factory.AbstractMsgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Darion Mograine H
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
    public Object handleMsg(MessageVO msgVO) {
        this.saveMessage(msgVO);
        Object response = this.handleMessage(msgVO);
        if (response instanceof TextMessageVO) {
            this.savePreTextMsg((TextMessageVO) response);
        }
        return response;
    }

    private void saveMessage(MessageVO msgVO) {
        Message message = new Message();
        BeanUtils.copyProperties(msgVO, message);
        messageDAO.save(message);
    }

    /**
     * 保存用户当前收到的消息的文本内容，用作后面查询用户的上一条消息，5分钟内有效
     *
     * @param response 返回给用户的消息体
     */
    private void savePreTextMsg(TextMessageVO response) {
        redisService.set(PRE_MESSAGE_PREFIX + response.getToUserName(), response.getContent(), 5 * 60);
    }
}
