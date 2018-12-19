package com.zereao.wechat.service.welcome;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.dto.NewsMessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Zereao
 * @version 2018/12/19  20:46
 */
@Service
public class WelcomeService {
    @Value("${welcom.msg.title}")
    private String title;
    @Value("${welcom.msg.banner}")
    private String bannerUrl;
    @Value("${welcom.msg.description}")
    private String description;
    @Value("${welcom.msg.url}")
    private String detail;

    public NewsMessageDTO getWelcomeArticle(String toUserName) {
        NewsMessageDTO.Articles.Item item = NewsMessageDTO.Articles.Item.builder()
                .title(title).picUrl(bannerUrl).description(description).url(detail).build();
        NewsMessageDTO.Articles articles = NewsMessageDTO.Articles.builder().item(item).build();
        return NewsMessageDTO.builder().articleCount(1).articles(articles)
                .toUserName(toUserName).msgType(MsgType.NEWS)
                .fromUserName("oQNld1W7beFJgJ_CfuGE1_tKVZZ4").createTime(new Date()).build();
    }
}
