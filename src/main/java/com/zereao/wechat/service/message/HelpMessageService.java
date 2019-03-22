package com.zereao.wechat.service.message;

import com.zereao.wechat.common.annotation.Command;
import com.zereao.wechat.common.config.CommonConfig;
import com.zereao.wechat.common.holder.CommandsHolder;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 帮助信息Service
 *
 * @author Darion Mograine H
 * @version 2018/12/21  14:03
 */
@Service
public class HelpMessageService {
    private String title;
    private String bannerUrl;
    private String description;
    private String detail;
    private String errorMsg;
    private String rootMsg;
    private String helpMsg;
    private String permissionErrorMsg;
    private String commonCmd;
    private String imgErrorMsg;


    @Autowired
    public HelpMessageService(CommonConfig commonConfig) {
        CommonConfig.Help help = commonConfig.getHelp();
        CommonConfig.Welcome welcome = commonConfig.getWelcome();
        this.title = welcome.getTitle();
        this.bannerUrl = welcome.getBanner();
        this.description = welcome.getDescription();
        this.detail = welcome.getUrl();
        this.errorMsg = help.getErrorMsg();
        this.rootMsg = help.getRootMsg();
        this.helpMsg = help.getCommonMsg();
        this.permissionErrorMsg = help.getPermissionErrorMsg();
        this.commonCmd = commonConfig.getMenu().getCommonCmd();
        this.imgErrorMsg = commonConfig.getImg().getErrorMsg();
    }


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
     * 获取 图片等待状态错误的信息
     *
     * @param toUserName 接收人的openID
     * @return 提示信息
     */
    public TextMessageVO getImgReadyErrorMsg(String toUserName) {
        return TextMessageVO.builder().toUserName(toUserName).content(imgErrorMsg).build();
    }

    /**
     * 抽离出的公共方法，实际逻辑是获取命令容器CommandsHolder中所有的L1级命令，并组装成菜单TextMessageVO
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
