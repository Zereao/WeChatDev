package com.zereao.wechat.service.command;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * 帮助命令Service
 *
 * @author Zereao
 * @version 2018/12/21  14:03
 */
@Slf4j
@Service
public class HelpCommandService extends AbstractCommandService {
    @Value("${wechat.from.openid}")
    private String fromUser;
    @Value("${help.error.msg}")
    private String errorMsg;
    @Value("classpath:data/commands.json")
    private Resource resource;
    @Value("${welcome.msg.title}")
    private String title;
    @Value("${welcome.msg.banner}")
    private String bannerUrl;
    @Value("${welcome.msg.description}")
    private String description;
    @Value("${welcome.msg.url}")
    private String detail;


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
        try {
            JSONObject commandsJson = JSONObject.parseObject(resource.getInputStream(), Charset.forName("utf-8"), JSONObject.class);
            StringBuilder content = new StringBuilder(commandsJson.getString("title"));
            commandsJson.getJSONObject("command").forEach((key, value) -> content.append("\n").append(key).append("：").append(value));
            content.append("\n").append(commandsJson.getString("ps"));
            return TextMessageVO.builder().createTime(new Date()).fromUserName(fromUser)
                    .msgType(MsgType.TEXT).toUserName(toUserName).content(content.toString()).build();
        } catch (IOException e) {
            log.error("读取commands.json 文件失败！", e);
        }
        return this.getErrorMsg(toUserName);
    }

    public TextMessageVO getErrorMsg(String toUserName) {
        return TextMessageVO.builder().toUserName(toUserName).fromUserName(fromUser)
                .msgType(MsgType.TEXT).createTime(new Date()).content(errorMsg).build();
    }
}
