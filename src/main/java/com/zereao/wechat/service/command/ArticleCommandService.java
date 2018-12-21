package com.zereao.wechat.service.command;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import com.zereao.wechat.dao.ArticlesDAO;
import com.zereao.wechat.data.bo.Articles;
import com.zereao.wechat.data.vo.NewsMessageVO;
import com.zereao.wechat.data.vo.TextMessageVO;
import com.zereao.wechat.data.vo.MessageVO;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zereao
 * @version 2018/12/19  20:33
 */
@Slf4j
@Service
public class ArticleCommandService extends AbstractCommandService {
    private final ArticlesDAO articlesDAO;
    private final RedisService redisService;

    private static Pattern articleIdPattern = Pattern.compile("id=(.*)&");

    @Value("${youdao.getinfo.url}")
    private String infoBaseUrl;
    @Value("${article.img.baseurl}")
    private String imgUrl;
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


    @Autowired
    public ArticleCommandService(ArticlesDAO articlesDAO, RedisService redisService) {
        this.articlesDAO = articlesDAO;
        this.redisService = redisService;
    }

    public TextMessageVO addArticle(MessageVO msgVO) {
        String url = msgVO.getContent().split("\\[wdxpn]|\\[WDXPN]")[1];
        Matcher matcher = articleIdPattern.matcher(url);
        String articleId = matcher.find() ? matcher.group(1) : "";
        String content = "文章添加失败！";
        try {
            String result = OkHttp3Utils.INSTANCE.doGet(infoBaseUrl.replace("{}", articleId));
            JSONObject response = JSONObject.parseObject(result);
            String title = response.getString("tl");
            String text = Jsoup.parse(response.getString("content")).text();
            Articles article = Articles.builder().createTime(new Date()).title(title)
                    .content(text).url(url).articleId(articleId).build();
            articlesDAO.save(article);
            content = "文章【" + title + "】添加成功！";
        } catch (IOException e) {
            log.warn("-----> 获取有道云笔记信息失败！", e);
        }
        return TextMessageVO.builder().toUserName(msgVO.getFromUserName()).fromUserName(fromUser)
                .msgType(MsgType.TEXT).createTime(new Date()).content(content).build();
    }

    /**
     * 获取所有的文章(标题)
     *
     * @param msgVO 包含所需参数的消息体
     */
    public TextMessageVO getAllArticles(MessageVO msgVO) {
        StringBuilder content = new StringBuilder("您可以回复文章标题前面的代码查看文章~\n");
        List<Articles> articlesList = articlesDAO.findAll();
        for (int i = 1, size = articlesList.size(); i < size + 1; i++) {
            // redis key 的格式， openid|index
            String redisKey = msgVO.getFromUserName() + "|" + i;
            // 将 文章ID信息放入 redis，5分钟内有效
            Articles articles = articlesList.get(i - 1);
            redisService.set(redisKey, articles.getId(), 5 * 60);
            content.append("1-").append(i).append(":").append(articles.getTitle()).append("\n");
        }
        return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                .toUserName(msgVO.getFromUserName()).content(content.toString()).build();
    }

    public Object getArticle(MessageVO msgVO) {
        String redisKey = msgVO.getFromUserName().concat("|").concat(msgVO.getContent().split("-")[1]);
        String articleId = String.valueOf(redisService.get(redisKey));
        Articles article = articlesDAO.findById(articleId).orElse(null);
        String toUser = msgVO.getFromUserName();
        if (article == null) {
            return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                    .toUserName(toUser).content("文章不存在哦~请检查您发送的代码是否正确~").build();
        }
        String picUrl = imgUrl.replace("{}", String.valueOf(RandomUtils.nextInt(1, 13)));
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title(article.getTitle()).picUrl(picUrl)
                .url(article.getUrl()).description(article.getContent().substring(0, 27).concat("....\n\n查看全文")).build();
        NewsMessageVO.Articles articles = NewsMessageVO.Articles.builder().item(item).build();
        return NewsMessageVO.builder().articles(articles).msgType(MsgType.NEWS).toUserName(toUser).fromUserName(fromUser)
                .articleCount(1).createTime(new Date()).build();
    }

    /**
     * 获取首次登陆时的欢迎信息
     *
     * @param toUserName 接收人的opnenId
     * @return 欢迎信息
     */
    public NewsMessageVO getWelcomeArticle(String toUserName) {
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder()
                .title(title).picUrl(bannerUrl).description(description).url(detail).build();
        NewsMessageVO.Articles articles = NewsMessageVO.Articles.builder().item(item).build();
        return NewsMessageVO.builder().articleCount(1).articles(articles)
                .toUserName(toUserName).msgType(MsgType.NEWS)
                .fromUserName(fromUser).createTime(new Date()).build();
    }
}
