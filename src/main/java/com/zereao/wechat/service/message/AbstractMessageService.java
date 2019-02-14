package com.zereao.wechat.service.message;

import com.zereao.wechat.dao.MessageDAO;
import com.zereao.wechat.pojo.po.Message;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.factory.AbstractMsgService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Darion Mograine H
 * @version 2018/12/11  18:20
 */
public abstract class AbstractMessageService extends AbstractMsgService {
    public static final String ROOT_ENABLED_PREFIX = "REDIS_KEY_OF_ROOT_";

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
        return this.handleMessage(msgVO);
    }

    private void saveMessage(MessageVO msgVO) {
        Message message = new Message();
        BeanUtils.copyProperties(msgVO, message);
        messageDAO.save(message);
    }

}
