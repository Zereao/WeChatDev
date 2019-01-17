package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.dao.MessageDAO;
import com.zereao.wechat.pojo.po.Message;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.command.HelpCommandService;
import com.zereao.wechat.service.factory.AbstractMsgService;
import com.zereao.wechat.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @author Zereao
 * @version 2018/12/11  18:20
 */
public abstract class AbstractMessageService extends AbstractMsgService {
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private HelpCommandService helpCommandService;
    @Autowired
    private RedisService redisService;

    private static final String REDIS_KEY_PREFIX = "COMMAND_OF_";

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
        Object result = this.checkCommand(msgVO);
        return result == null ? this.handleMessage(msgVO) : result;
    }

    private void saveMessage(MessageVO msgVO) {
        Message message = new Message();
        BeanUtils.copyProperties(msgVO, message);
        messageDAO.save(message);
    }

    private Object checkCommand(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        String command = msgVO.getContent().trim();
        String redisKey = REDIS_KEY_PREFIX + openid;
        String existsCommand = redisService.get(redisKey);
        String newCommand;
        String targetCommand = StringUtils.isBlank(existsCommand) ?
                (CommandsHolder.contains(command) ? command : null) :
                (CommandsHolder.contains(newCommand = existsCommand.concat("-").concat(command)) ? newCommand : null);
        return Optional.ofNullable(targetCommand).map(c -> {
            redisService.set(redisKey, c, 5 * 60);
            return null;
        }).orElse(helpCommandService.getHelp(openid));
    }
}
