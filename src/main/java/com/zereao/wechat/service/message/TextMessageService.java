package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.dao.UserDAO;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.command.AbstractCommandService;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 处理接收到的文本消息的Service
 *
 * @author Darion Mograine H
 * @version 2018/12/11  20:03
 */
@Slf4j
@Service
public class TextMessageService extends AbstractMessageService {
    private final Map<String, AbstractCommandService> commandServiceMap;
    private final HelpMessageService helpMessageService;
    private final RedisService redisService;

    private final UserDAO userDAO;

    private static final String COMMAND_PREFIX = "COMMAND_OF_";
    private static final String COMMAND_ROOT = "wdxpn";
    private static final String COMMAND_FIRST_PAGE = "#";
    private static final String COMMAND_PRE_PAGE = "-";


    @Autowired
    public TextMessageService(Map<String, AbstractCommandService> commandServiceMap, HelpMessageService helpMessageService, RedisService redisService, UserDAO userDAO) {
        this.commandServiceMap = commandServiceMap;
        this.helpMessageService = helpMessageService;
        this.redisService = redisService;
        this.userDAO = userDAO;
    }

    @PostConstruct
    public void cleanMenuTree() {
        log.info("----->  准备清理菜单树...");
        this.userDAO.findAll().forEach(user -> {
            String openid = user.getOpenid();
            this.redisService.del(COMMAND_PREFIX + openid);
            this.redisService.del(ROOT_ENABLED_PREFIX + openid);
        });
        log.info("----->  菜单树清理完毕~");
    }

    @Override
    public Object handleMessage(MessageVO msgVO) {
        Object result = this.checkCommand(msgVO);
        if (result == null) {
            String content = msgVO.getContent();
            String openid = msgVO.getFromUserName();
            CommandsHolder.Command command;
            if (content.contains("*")) {
                command = CommandsHolder.get(content.substring(0, content.lastIndexOf("*") + 1));
            } else {
                command = CommandsHolder.get(content);
            }
            if (Command.MenuType.ROOT.equals(command.menu) && !StringUtils.equals(redisService.get(ROOT_ENABLED_PREFIX + openid), "true")) {
                return helpMessageService.getPermissionErrorMsg(openid);
            }
            return commandServiceMap.get(command.bean).exec(msgVO, command);
        }
        return result;
    }

    /**
     * 非常核心的方法，用来处理 命令组装与匹配
     *
     * @param msgVO 包含相关参数的MessageVO
     * @return null or TextMessageVO-Help Message
     */
    private Object checkCommand(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        String userCommand = msgVO.getContent();
        String redisKey = COMMAND_PREFIX + openid;
        String existedCommand = redisService.get(redisKey);

        switch (userCommand) {
            // 开启ROOT权限命令
            case COMMAND_ROOT:
                redisService.set(ROOT_ENABLED_PREFIX + openid, "true", 5 * 60);
                redisService.del(redisKey);
                return helpMessageService.getRootMsg(openid);
            // 返回首页命令
            case COMMAND_FIRST_PAGE:
                redisService.del(redisKey);
                return helpMessageService.getHelp(openid);
            // 返回上一页 命令
            case COMMAND_PRE_PAGE:
                if (StringUtils.isNotBlank(existedCommand) && existedCommand.contains("-")) {
                    existedCommand = existedCommand.substring(0, existedCommand.lastIndexOf("-"));
                    redisService.set(redisKey, existedCommand, 5 * 60);
                    msgVO.setContent(existedCommand);
                } else {
                    redisService.del(redisKey);
                    return helpMessageService.getHelp(openid);
                }
                return null;
            // 常规命令
            default:
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
                    return helpMessageService.getHelp(openid);
                } else {
                    msgVO.setContent(targetCommand);
                    if (updateRedis) {
                        redisService.set(redisKey, targetCommand, 5 * 60);
                    }
                    return null;
                }
        }
    }
}
