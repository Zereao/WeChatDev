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
    public static final String ROOT_ENABLED = "REDIS_KEY_OF_ROOT";

    @Override
    public Object handleMessage(MessageVO msgVO) {
        Object result = this.checkCommand(msgVO);
        if (result == null) {
            String content = msgVO.getContent();
            CommandsHolder.Command command;
            if (content.contains("*")) {
                command = CommandsHolder.get(content.substring(0, content.lastIndexOf("*") + 1));
            } else {
                command = CommandsHolder.get(content);
            }
            return commandServiceMap.get(command.bean).exec(msgVO, command);
        }
        return result;
    }

    /**
     * 非常核心的方法，用来处理 命令组装与匹配
     * 检查命令是否在Redis中存在
     *
     * @param msgVO 包含相关参数的MessageVO
     * @return null or TextMessageVO-Help Message
     */
    private Object checkCommand(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        String userCommand = msgVO.getContent();
        if ("wdxpn".equalsIgnoreCase(userCommand)) {
            redisService.set(ROOT_ENABLED, "true", 5 * 60);
            return helpCommandService.getRootMsg(openid);
        }
        String redisKey = REDIS_KEY_PREFIX + openid;
        String existedCommand = redisService.get(redisKey);
        String targetCommand = null;
        boolean updateRedis = true;
        if (StringUtils.isBlank(existedCommand)) {
            if (CommandsHolder.contains(userCommand)) {
                targetCommand = userCommand;
            }
        } else {
            String newCommand = existedCommand + "-" + userCommand;
            if (CommandsHolder.contains(newCommand)) {
                targetCommand = newCommand;
            } else {
                newCommand = existedCommand + "-" + "*";
                if (CommandsHolder.contains(newCommand)) {
                    updateRedis = false;
                    targetCommand = newCommand + userCommand;
                }
            }
        }
        if (targetCommand == null) {
            // 命令不存在时，刷新 菜单树
            redisService.del(redisKey);
            return helpCommandService.getHelp(openid);
        } else {
            msgVO.setContent(targetCommand);
            if (updateRedis) {
                redisService.set(redisKey, targetCommand, 5 * 60);
            }
            return null;
        }
    }
}
