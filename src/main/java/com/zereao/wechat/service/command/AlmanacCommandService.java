package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.annotation.Command.MenuType;
import com.zereao.wechat.commom.annotation.Command.Level;
import com.zereao.wechat.commom.annotation.resolver.CommandsHolder;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Darion Mograine H
 * @version 2019/02/12  15:58
 */
@Service
public class AlmanacCommandService extends AbstractCommandService {
    @Value("${almanac.url}")
    private String url;
    @Value("${menu.header.info}")
    private String header;
    @Value("${almanac.img.lucky.url}")
    private String luckyImg;
    @Value("${almanac.img.rest.url}")
    private String restImg;

    @Command(mapping = "2", name = "老爹的黄历", level = Level.L1, menu = MenuType.USER)
    public TextMessageVO getFatherAlmanac(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        Map<String, String> commandMap = CommandsHolder.list(openid, this.getClass(), Level.L2);
        StringBuilder sb = new StringBuilder(header).append("\n");
        int i = 1;
        for (String commandName : commandMap.keySet()) {
            sb.append("\n").append(i++).append("：").append(commandName);
        }
        sb.append(commonCmd);
        return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                .toUserName(openid).content(sb.toString()).build();
    }


    @Command(mapping = "2-1", name = "今日运势-网页版", level = Level.L2, menu = MenuType.USER)
    public TextMessageVO obWithHtml(MessageVO msgVO) {

//        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title("今日运势").picUrl(picUrl)
//                .url(article.getUrl()).description(article.getContent().substring(0, 37).concat("....\n\n查看全文")).build();
//        NewsMessageVO.Articles articles = NewsMessageVO.Articles.builder().item(item).build();
//        return NewsMessageVO.builder().articles(articles).msgType(MsgType.NEWS).toUserName(toUser).fromUserName(fromUser)
//                .articleCount(1).createTime(new Date()).build();
        return null;
    }


}
