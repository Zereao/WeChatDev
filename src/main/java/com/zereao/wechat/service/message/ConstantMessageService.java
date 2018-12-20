package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.dto.NewsMessageDTO;
import com.zereao.wechat.data.dto.TextMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 一节常用的提示信息组装Service
 *
 * @author Zereao
 * @version 2018/12/20  10:59
 */
@Slf4j
@Service
public class ConstantMessageService {
    @Value("${wechat.from.openid}")
    private String fromUser;
    @Value("${welcome.msg.title}")
    private String title;
    @Value("${welcome.msg.banner}")
    private String bannerUrl;
    @Value("${welcome.msg.description}")
    private String description;
    @Value("${welcome.msg.url}")
    private String detail;
    @Value("${help.msg.info}")
    private String helpInfo;
    @Value("${help.error.msg}")
    private String errorMsg;

    /**
     * 获取首次登陆时的欢迎信息
     *
     * @param toUserName 接收人的opnenId
     * @return 欢迎信息
     */
    public NewsMessageDTO getWelcomeArticle(String toUserName) {
        NewsMessageDTO.Articles.Item item = NewsMessageDTO.Articles.Item.builder()
                .title(title).picUrl(bannerUrl).description(description).url(detail).build();
        NewsMessageDTO.Articles articles = NewsMessageDTO.Articles.builder().item(item).build();
        return NewsMessageDTO.builder().articleCount(1).articles(articles)
                .toUserName(toUserName).msgType(MsgType.NEWS)
                .fromUserName(fromUser).createTime(new Date()).build();
    }

    /**
     * 获取帮助信息
     *
     * @param toUserName 接收人的openID
     * @return 帮助信息
     */
    public TextMessageDTO getHelp(String toUserName) {
        return TextMessageDTO.builder().createTime(new Date()).fromUserName(fromUser)
                .msgType(MsgType.TEXT).toUserName(toUserName).content(helpInfo).build();
    }

    public TextMessageDTO getErrorMsg(String toUserName) {
        return TextMessageDTO.builder().toUserName(toUserName).fromUserName(fromUser)
                .msgType(MsgType.TEXT).createTime(new Date()).content(errorMsg).build();
    }
}
