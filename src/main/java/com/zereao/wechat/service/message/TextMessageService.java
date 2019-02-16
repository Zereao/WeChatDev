package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.command.AbstractCommandService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final String COMMAND_ROOT = "wdxpn";
    private static final String COMMAND_FIRST_PAGE = "#";
    private static final String COMMAND_PRE_PAGE = "-";

    @Autowired
    public TextMessageService(Map<String, AbstractCommandService> commandServiceMap) {
        this.commandServiceMap = commandServiceMap;
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
        String redisKey = COMMAND_TEEE_PREFIX + openid;
        String existedCommand = redisService.get(redisKey);

        switch (userCommand) {
            /* 开启ROOT权限  命令，执行以下操作：
                1、将当前用户置为Root状态
                2、清理掉当前用户的菜单树；（可能之前的菜单树中不存在ROOT功能）
                3、返回获得Root权限后的帮助信息    */
            case COMMAND_ROOT:
                redisService.set(ROOT_ENABLED_PREFIX + openid, "true", 5 * 60);
                redisService.del(redisKey);
                return helpMessageService.getRootMsg(openid);
            /* 返回首页  命令，执行以下操作：
                1、清理掉当前用户的菜单树；（可能之前的菜单树中不存在ROOT功能）
                2、返回帮助信息    */
            case COMMAND_FIRST_PAGE:
                redisService.del(redisKey);
                return helpMessageService.getHelp(openid);
            /* 返回上一页   命令，分两种情况：
                一、如果Redis中已经存在命令树，并且包含分隔符 - ：（说明命令树中至少存在两级菜单），执行：
                    1、更新 Redis中已存在的命令为 当前已存在命令的上一级命令，5分钟内有效
                    2、更新MessageVO的内容为 上面更新后的 上一级命令，用于接下来程序执行
                    3、返回 null，则程序继续解析、执行上面的MessageVO中设定的命令
                二、如果Redis中不存在命令树，则执行：
                    1、直接返回 帮助信息给用户     */
            case COMMAND_PRE_PAGE:
                if (StringUtils.isNotBlank(existedCommand) && existedCommand.contains("-")) {
                    existedCommand = existedCommand.substring(0, existedCommand.lastIndexOf("-"));
                    redisService.set(redisKey, existedCommand, 5 * 60);
                    msgVO.setContent(existedCommand);
                } else {
                    return helpMessageService.getHelp(openid);
                }
                return null;
            /* 常规命令，分为两种情况：
                一、如果Redis中不存在当前用户的命令树：
                    1、同时命令容器CommandsHolder中存在当前用户输入的命令，将当前命令存入Redis命令树中；
                二、如果Redis中已经存在当前用户的命令树，则又分为两种情况：
                    首先，拼接好新的命令；
                    1、如果，CommandsHolder中包含新的命令，将新的命令更新进Redis命令树中；
                    2、否则，再判断当前命令是否对应 通配命令——已存在的命令-*，如果对应是通配命令，则不更新Redis中的命令树，
                        同时执行【已存在的命令-* + 用户命令】

                    PS：针对上面的 二.2 情况，如果当前命令对应统配命令，而用户发送的命令实际不存在，则具体的命令树处理逻辑由具体业务处理，
                    此情况只针对 通配命令     */
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
