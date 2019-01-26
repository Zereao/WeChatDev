package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.command.AbstractCommandService;
import com.zereao.wechat.service.command.HelpCommandService;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * 处理接收到的文本消息的Service
 *
 * @author Zereao
 * @version 2018/12/11  20:03
 */
@Slf4j
@Service
public class TextMessageService extends AbstractMessageService {
    private final Map<String, AbstractCommandService> commandServiceMap;
    private final HelpCommandService helpCommandService;
    private final RedisService redisService;

    @Autowired
    public TextMessageService(Map<String, AbstractCommandService> commandServiceMap, HelpCommandService helpCommandService, RedisService redisService) {
        this.commandServiceMap = commandServiceMap;
        this.helpCommandService = helpCommandService;
        this.redisService = redisService;
    }

    private static final String REDIS_KEY_PREFIX = "COMMAND_OF_";

    @Override
    public Object handleMessage(MessageVO msgVO) {
        Object result = this.checkCommand(msgVO);
        if (result == null) {
            CommandsHolder.Command command = CommandsHolder.get(msgVO.getContent());
            return commandServiceMap.get(command.bean).exec(msgVO, command);
        }
        return result;
    }

    /**
     * 检查命令是否在Redis中存在
     *
     * @param msgVO 包含相关参数的MessageVO
     * @return null or TextMessageVO-Help Message
     */
    private Object checkCommand(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        String command = msgVO.getContent().trim();
        String redisKey = REDIS_KEY_PREFIX + openid;
        String existsCommand = redisService.get(redisKey);
        String newCommand;
        String targetCommand = StringUtils.isBlank(existsCommand) ? (CommandsHolder.contains(command) ? command : null) : (CommandsHolder.contains(newCommand = existsCommand.concat("-").concat(command)) ? newCommand : null);
        if (targetCommand == null) {
            return helpCommandService.getHelp(openid);
        } else {
            msgVO.setContent(targetCommand);
            redisService.set(redisKey, targetCommand, 5 * 60);
            return null;
        }
    }
}
