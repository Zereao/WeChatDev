package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 帮助信息Service
 *
 * @author Zereao
 * @version 2018/12/21  14:03
 */
@Slf4j
@Service
public class HelpMessageService {
    @Value("${wechat.from.openid}")
    private String fromUser;
    @Value("${help.error.msg}")
    private String errorMsg;
    @Value("${welcome.msg.title}")
    private String title;
    @Value("${welcome.msg.banner}")
    private String bannerUrl;
    @Value("${welcome.msg.description}")
    private String description;
    @Value("${welcome.msg.url}")
    private String detail;
    @Value("${help.root.msg}")
    private String rootMsg;

    private final RedisService redisService;

    @Autowired
    public HelpMessageService(RedisService redisService) {this.redisService = redisService;}

    /**
     * 获取首次登陆时的欢迎信息
     *
     * @param msgVO 包含所需参数的消息体
     * @return 欢迎信息
     */
    public NewsMessageVO getWelcomeArticle(MessageVO msgVO) {
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder()
                .title(title).picUrl(bannerUrl).description(description).url(detail).build();
        NewsMessageVO.Articles articles = NewsMessageVO.Articles.builder().item(item).build();
        return NewsMessageVO.builder().articleCount(1).articles(articles)
                .toUserName(msgVO.getFromUserName()).msgType(MsgType.NEWS)
                .fromUserName(fromUser).createTime(new Date()).build();
    }

    /**
     * 获取帮助信息
     *
     * @param toUserName 接收人的openID
     * @return 帮助信息
     */
    public TextMessageVO getHelp(String toUserName) {
        StringBuilder content = new StringBuilder("Hey!您的消息我已经收到啦！~您可以回复功能列表前的代码，使用相应的功能哦~\n");
        Command.MenuType menu = Command.MenuType.USER;
        if ("true".equals(redisService.get(TextMessageService.ROOT_ENABLED))) {
            menu = Command.MenuType.ROOT;
        }
        CommandsHolder.list(menu, true).forEach((k, v) -> content.append("\n").append(v).append("：").append(k));
        return TextMessageVO.builder().createTime(new Date()).fromUserName(fromUser)
                .msgType(MsgType.TEXT).toUserName(toUserName).content(content.toString()).build();
    }

    /**
     * 获取错误信息
     *
     * @param toUserName 接收人的openID
     * @return 错误信息
     */
    public TextMessageVO getErrorMsg(String toUserName) {
        return TextMessageVO.builder().toUserName(toUserName).fromUserName(fromUser)
                .msgType(MsgType.TEXT).createTime(new Date()).content(errorMsg).build();
    }

    /**
     * 获取root权限时的提示信息
     *
     * @param toUserName 接收人的openID
     * @return 提示信息
     */
    public TextMessageVO getRootMsg(String toUserName) {
        StringBuilder content = new StringBuilder(rootMsg + "\n");
        Command.MenuType menu = "true".equals(redisService.get(TextMessageService.ROOT_ENABLED)) ? Command.MenuType.ROOT : Command.MenuType.USER;
        CommandsHolder.list(menu, true).forEach((k, v) -> content.append("\n").append(v).append("：").append(k));
        return TextMessageVO.builder().toUserName(toUserName).fromUserName(fromUser)
                .msgType(MsgType.TEXT).createTime(new Date()).content(content.toString()).build();
    }
}
