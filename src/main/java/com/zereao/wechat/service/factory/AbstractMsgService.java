package com.zereao.wechat.service.factory;


import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.message.HelpMessageService;
import com.zereao.wechat.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象顶级消息处理类
 *
 * @author Zereao
 * @version 2018/12/13  14:52
 */
public abstract class AbstractMsgService {
    @Autowired
    protected RedisService redisService;
    @Autowired
    protected HelpMessageService helpMessageService;

    // 用户是否启用ROOT 权限 redis key前缀
    protected static final String ROOT_ENABLED_PREFIX = "ROOT_ENABLE_OF_";
    // 用户正在等待图片消息 redis Key 前缀
    protected static final String IMG_READY_PREFIX = "IMG_READY_OF_";
    // 用户的命令树 redis Key 前缀
    protected static final String COMMAND_TEEE_PREFIX = "COMMAND_OF_";
    // 用户的上一条返回的文本消息的内容 redis Key 前缀
    protected static final String PRE_MESSAGE_PREFIX = "PRE_MSG_OF_";

    /**
     * 处理消息，并按需做出返回
     *
     * @param msgVO 包含所有的参数的 MsgVO
     * @return 返回体
     */
    public abstract Object handleMsg(MessageVO msgVO);

    /**
     * 清理用户的菜单树
     *
     * @param openid 用户的openid
     */
    protected void cleanCommandTree(String openid) {
        redisService.del(COMMAND_TEEE_PREFIX + openid);
    }

    /**
     * 获取当前用户的上一条返回的信息；如果信息不存在，则返回帮助信息
     *
     * @param openid 当前用户的openid
     * @return 当前用户的上一条返回信息；如果信息不存在，则返回帮助信息
     */
    protected TextMessageVO getPreMsg(String openid) {
        String preMsg = redisService.get(PRE_MESSAGE_PREFIX + openid);
        return StringUtils.isBlank(preMsg) ? helpMessageService.getHelp(openid) : TextMessageVO.builder().toUserName(openid).content(preMsg).build();
    }
}
