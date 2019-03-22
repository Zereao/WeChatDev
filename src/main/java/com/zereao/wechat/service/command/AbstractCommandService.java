package com.zereao.wechat.service.command;

import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.config.CommonConfig;
import com.zereao.wechat.common.holder.CommandsHolder;
import com.zereao.wechat.common.holder.CommandsHolder.Command;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.message.HelpMessageService;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Darion Mograine H
 * @version 2018/12/19  20:21
 */
@Slf4j
public abstract class AbstractCommandService {
    @Autowired
    public HelpMessageService helpMessageService;
    @Autowired
    protected RedisService redisService;
    @Autowired
    private CommonConfig commonConfig;

    // 某个用户的 命令树  redis Key 前缀
    private static final String COMMAND_TREE_PREFIX = "COMMAND_OF_";
    // 某个用户正在等待图片消息 redis Key 前缀
    protected static final String IMG_READY_PREFIX = "IMG_READY_OF_";

    protected String commonCmd;
    protected String header;

    @PostConstruct
    public void init() {
        CommonConfig.Menu menu = commonConfig.getMenu();
        commonCmd = menu.getCommonCmd();
        header = menu.getHeaderInfo();
    }

    /**
     * 执行命令
     *
     * @param msgVO 封装了参数的消息实体
     * @return 返回值
     */
    public Object exec(MessageVO msgVO, Command command) {
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

    /**
     * 获取当前类的 level 级菜单
     *
     * @param msgVO 包含信息的MessageVO
     * @param level 菜单等级
     * @return 菜单消息
     */
    protected TextMessageVO getMenu(MessageVO msgVO, Level level) {
        String openid = msgVO.getFromUserName();
        Map<String, String> commandMap = CommandsHolder.list(openid, this.getClass(), level);
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

    /**
     * 手动将当前用户的命令树更新为上一级命令
     *
     * @param openid 用户的openid
     */
    protected void set2PreCommand(String openid) {
        String redisKey = COMMAND_TREE_PREFIX + openid;
        String existedCommand = redisService.get(redisKey);
        existedCommand = existedCommand.substring(0, existedCommand.lastIndexOf("-"));
        redisService.set(redisKey, existedCommand, 5 * 60);
    }
}