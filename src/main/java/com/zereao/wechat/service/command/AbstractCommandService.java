package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.message.HelpMessageService;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Darion Mograine H
 * @version 2018/12/19  20:21
 */
@Slf4j
public abstract class AbstractCommandService {
    @Autowired
    private HelpMessageService helpMessageService;
    @Autowired
    protected RedisService redisService;

    @Value("${wechat.from.openid}")
    protected String fromUser;
    @Value("${menu.common.cmd}")
    protected String commonCmd;
    @Value("${menu.header.info}")
    protected String header;

    // 某个用户的 命令树  redis Key 前缀
    private static final String COMMAND_TREE_PREFIX = "COMMAND_OF_";
    // 某个用户正在等待图片消息 redis Key 前缀
    protected static final String IMG_READY_PREFIX = "IMG_READY_OF_";

    /**
     * 执行命令
     *
     * @param msgVO 封装了参数的消息实体
     * @return 返回值
     */
    public Object exec(MessageVO msgVO, CommandsHolder.Command command) {
        Class<?> curClass = this.getClass();
        String className = curClass.getName();
        log.info("------->  {}准备执行命令 {}", className, command.toString());
        try {
            return command.method.invoke(this, msgVO);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("执行方法 {} 失败！\n{}", command.method.getName(), e);
            return helpMessageService.getErrorMsg(msgVO.getFromUserName());
        }
    }

    /**
     * 清理当前用户的菜单树
     *
     * @param openid 用户openid
     */
    protected void cleanCommand(String openid) {
        String redisKey = COMMAND_TREE_PREFIX + openid;
        redisService.del(redisKey);
    }

    protected TextMessageVO getMenu(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        Map<String, String> commandMap = CommandsHolder.list(openid, this.getClass(), Command.Level.L2);
        StringBuilder sb = new StringBuilder(header).append("\n");
        int i = 1;
        for (String commandName : commandMap.keySet()) {
            sb.append("\n").append(i++).append("：").append(commandName);
        }
        sb.append(commonCmd);
        return TextMessageVO.builder().toUserName(openid).content(sb.toString()).build();
    }

    protected void imgReady(String openid) {
        redisService.set(IMG_READY_PREFIX + openid, "true", 2 * 60);
    }
}