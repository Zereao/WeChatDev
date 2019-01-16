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

    private TextMessageVO checkCommand(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        String command = msgVO.getContent().trim();
        String redisKey = REDIS_KEY_PREFIX + openid;
        String existsCommand = redisService.get(redisKey);
        // redis 中不存在当前用户发送的命令
        boolean commandNotExist = StringUtils.isBlank(existsCommand);
        // redis 中存在当前用户之前发送的命令，但是新命令 不存在容器中
        String newCommand = commandNotExist ? command : existsCommand.concat("-").concat(command);
        boolean commandExist = !commandNotExist && CommandsHolder.contains(newCommand);
        // redis 中不存在用户之前发送的命令，这是一个新命令
        boolean commandNew = commandNotExist && CommandsHolder.contains(command);
        if (commandNew) {
            // 如果是一条新命令，将命令入库，五分钟有效
            redisService.set(redisKey, command, 5 * 60);
        }
        if (commandNew) {
            // 存在之前的命令，更新命令
            redisService.set(redisKey, newCommand, 5 * 60);
        }
        return commandExist || commandNew ? null : helpCommandService.getHelp(openid);
    }
}
