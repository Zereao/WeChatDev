package com.zereao.wechat.service.command;

import com.zereao.wechat.common.annotation.Command;
import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.utils.OkHttp3Utils;
import com.zereao.wechat.pojo.dto.AlmanacDTO;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Darion Mograine H
 * @version 2019/02/12  15:58
 */
@Slf4j
@Service
public class AlmanacCommandService extends AbstractCommandService {
    @Value("${almanac.url}")
    private String url;
    @Value("${almanac.img.lucky.url}")
    private String luckyImg;
    @Value("${almanac.img.rest.url}")
    private String restImg;
    @Value("${almanac.error.info}")
    private String errorInfo;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    private AlmanacCommandService commandService;

    private static final Pattern TIME_LUCK_PATTERN = Pattern.compile("时 (.*〗)");

    @Command(mapping = "2", name = "老爹的黄历", level = Level.L1)
    public TextMessageVO getFatherAlmanac(MessageVO msgVO) {
        return this.getMenu(msgVO, Level.L2);
    }


    @Command(mapping = "2-1", name = "今日运势-网页版", level = Level.L2)
    public NewsMessageVO obWithHtml(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        AlmanacDTO almanac = commandService.getAlmanacInfo();
        String picUrl = almanac.getSuitableList().size() > almanac.getTabooList().size() ? luckyImg : restImg;
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title("今日运势")
                .description(almanac.getDate()).url(url).picUrl(picUrl).build();
        return NewsMessageVO.builder().toUserName(openid).articles(new NewsMessageVO.Articles(item)).build();
    }

    @Command(mapping = "2-2", name = "今日宜忌", level = Level.L2)
    public TextMessageVO getLuck(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        AlmanacDTO almanac = commandService.getAlmanacInfo();
        int tag = 1;
        StringBuilder content = new StringBuilder("【今日老黄历宜】\n");
        for (String suitable : almanac.getSuitableList()) {
            content.append(suitable);
            if (tag++ % 4 == 0) {content.append("\n");} else {content.append("  ");}
        }
        content.append("\n\n").append("【今日老黄历忌】\n");
        tag = 1;
        for (String taboo : almanac.getTabooList()) {
            content.append(taboo);
            if (tag++ % 4 == 0) {content.append("\n");} else {content.append("  ");}
        }
        content.append(commonCmd);
        return TextMessageVO.builder().toUserName(openid).content(content.toString()).build();
    }

    @Command(mapping = "2-3", name = "时辰吉凶", level = Level.L2)
    public TextMessageVO getTimeLuck(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        AlmanacDTO almanac = commandService.getAlmanacInfo();
        StringBuilder content = new StringBuilder("【今日老黄历时辰吉凶】\n");
        for (String luck : almanac.getTimeLuck()) {
            content.append("\n").append(luck);
        }
        content.append(commonCmd);
        return TextMessageVO.builder().toUserName(openid).content(content.toString()).build();
    }

    @Cacheable("almanac")
    public AlmanacDTO getAlmanacInfo() {
        try {
            String html = OkHttp3Utils.doGetWithGBK(url);
            if (StringUtils.isBlank(html)) {
                log.warn("------> 请求[{}]获得的数据为null！", url);
                return null;
            }
            Document htmlDoc = Jsoup.parse(html);
            // 1、得到今日日期
            String date = htmlDoc.getElementsByClass("neirong1").select(".txt1").text();

            // 2、得到今日宜/忌
            List<String> suitableList = new ArrayList<>();
            List<String> tabooList = new ArrayList<>();
            Elements luckyDoSomething = htmlDoc.getElementsByClass("neirong_Yi_Ji");
            for (Element luck : luckyDoSomething) {
                // 运势名称  宜/忌
                String luckName = luck.previousElementSibling().previousElementSibling().text();
                String[] luckArray = luck.text().replaceAll("[ 　]+", " ").trim().split(" ");
                switch (luckName) {
                    case "【今日老黄历宜】":
                        Collections.addAll(suitableList, luckArray);
                        break;
                    case "【今日老黄历忌】":
                        Collections.addAll(tabooList, luckArray);
                        break;
                    default:
                        log.warn("------> 爬取到的信息有误，未能采集到相关信息！luckName = {}", luckName);
                        break;
                }
            }

            // 3、拿到时辰运势
            List<String> timeLuckList = new ArrayList<>();
            Elements timeLuck = htmlDoc.getElementsByClass("neirong_shichen");
            for (Element luck : timeLuck) {
                for (Element theTimeLuck : luck.children()) {
                    if (theTimeLuck.hasText()) {
                        Matcher matcher = TIME_LUCK_PATTERN.matcher(theTimeLuck.text());
                        if (matcher.find()) {
                            timeLuckList.add(matcher.group(1));
                        }
                    }
                }
            }
            return AlmanacDTO.builder().date(date).suitableList(suitableList).tabooList(tabooList).timeLuck(timeLuckList).build();
        } catch (IOException e) {
            log.error("访问[" + url + "]出错！", e);
        }
        return AlmanacDTO.builder().build();
    }
}
