package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 帮助信息Service
 *
 * @author Darion Mograine H
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
    @Value("${help.common.msg}")
    private String helpMsg;
    @Value("${help.permission.error.msg}")
    private String permissionErrorMsg;
    @Value("${menu.common.cmd}")
    private String commonCmd;

    /**
     * 获取首次登陆时的欢迎信息
     *
     * @param msgVO 包含所需参数的消息体
     * @return 欢迎信息
     */
    public NewsMessageVO getWelcomeArticle(MessageVO msgVO) {
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title(title).picUrl(bannerUrl).description(description).url(detail).build();
        return NewsMessageVO.builder().articles(new NewsMessageVO.Articles(item)).toUserName(msgVO.getFromUserName()).build();
    }

    /**
     * 获取帮助信息
     *
     * @param toUserName 接收人的openID
     * @return 帮助信息
     */
    public TextMessageVO getHelp(String toUserName) {
        return this.getTargetMsg(helpMsg, toUserName);
    }

    /**
     * 获取错误信息
     *
     * @param toUserName 接收人的openID
     * @return 错误信息
     */
    public TextMessageVO getErrorMsg(String toUserName) {
        return TextMessageVO.builder().toUserName(toUserName).content(errorMsg).build();
    }

    /**
     * 获取root权限时的提示信息
     *
     * @param toUserName 接收人的openID
     * @return 提示信息
     */
    public TextMessageVO getRootMsg(String toUserName) {
        return this.getTargetMsg(rootMsg, toUserName);
    }

    /**
     * 获取权限不足的提示信息
     *
     * @param toUserName 接收人的openID
     * @return 提示信息
     */
    public TextMessageVO getPermissionErrorMsg(String toUserName) {
        return TextMessageVO.builder().toUserName(toUserName).content(permissionErrorMsg).build();
    }

    /**
     * 抽离出的公共方法
     *
     * @param msg        消息头
     * @param toUserName 收件人
     * @return 返回消息
     */
    private TextMessageVO getTargetMsg(String msg, String toUserName) {
        StringBuilder content = new StringBuilder(msg).append("\n");
        CommandsHolder.list(toUserName, Command.Level.L1).forEach((k, v) -> content.append("\n").append(v).append("：").append(k));
        content.append(commonCmd);
        return TextMessageVO.builder().toUserName(toUserName).content(content.toString()).build();
    }
}
