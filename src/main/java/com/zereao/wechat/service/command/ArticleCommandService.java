package com.zereao.wechat.service.command;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import com.zereao.wechat.commom.utils.ThreadPoolUtils;
import com.zereao.wechat.dao.ArticlesDAO;
import com.zereao.wechat.pojo.po.Articles;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
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

    /**
     * 文章列表 - redisKey - 后缀
     */
    private String redisKeySuffix = "|article-list";

    @Autowired
    public ArticleCommandService(ArticlesDAO articlesDAO, RedisService redisService) {
        this.articlesDAO = articlesDAO;
        this.redisService = redisService;
    }

    /**
     * 获取所有的文章(标题)
     *
     * @param msgVO 包含所需参数的消息体
     */
    @Command(mapping = "1", name = "获取文章列表", first = true, menu = Command.MenuType.USER)
    public TextMessageVO getAllArticles(MessageVO msgVO) {
        List<Articles> articlesList = articlesDAO.findAll();
        StringBuilder content;
        if (articlesList.size() <= 0) {
            content = new StringBuilder("当前还有文章~快去撩撩伦哥让他添加吧~");
        } else {
            content = new StringBuilder("您可以回复文章标题前面的代码查看文章~\n");
            Map<String, String> articleMap = new HashMap<>();
            for (int i = 0, size = articlesList.size(); i < size; i++) {
                Articles articles = articlesList.get(i);
                articleMap.put(String.valueOf(i + 1), articles.getId());
                content.append("\n").append(i + 1).append("：").append(articles.getTitle());
            }
            // redis key 的格式， openid|article-list，5分钟内有效
            redisService.hmset(msgVO.getFromUserName() + redisKeySuffix, articleMap, 5 * 60);
            content.append("\n\n为了缓解服务器压力，文章代码5分钟内有效哦~");
        }
        return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                .toUserName(msgVO.getFromUserName()).content(content.toString()).build();
    }

    @Command(mapping = "1-*", name = "获取文章", menu = Command.MenuType.USER)
    public Object getArticle(MessageVO msgVO) {
        String toUser = msgVO.getFromUserName();
        Map<Object, Object> articleMap = redisService.hmget(toUser.concat(redisKeySuffix));
        if (articleMap == null || articleMap.size() <= 0) {
            return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                    .toUserName(toUser).content("文章列表缓存已过期，请重新操作~").build();
        }
        String articleId = String.valueOf(articleMap.get(msgVO.getContent().replace("1-*", "")));
        Articles article;
        if (StringUtils.isEmpty(articleId) || (article = articlesDAO.findById(articleId).orElse(null)) == null) {
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

    @Command(name = "新增文章", mapping = "r1", first = true, menu = Command.MenuType.ROOT)
    public TextMessageVO addArticle(MessageVO msgVO) {
        String[] urls = msgVO.getContent().replaceAll("1-root\\.add\\[wdxpn]|1-root\\.add\\[WDXPN]", "").split("\\[wdxpn]|\\[WDXPN]");
        CountDownLatch latch = new CountDownLatch(urls.length);
        StringBuilder content;
        try {
            content = new StringBuilder("文章");
            for (String url : urls) {
                String title = ThreadPoolUtils.submit(new AddArticleThread(url, latch));
                if (StringUtils.isEmpty(title)) {
                    log.error("文章[{}]添加失败！", url);
                } else {
                    content.append("【").append(title).append("】、");
                }
            }
            latch.await();
            content.append("添加成功！").deleteCharAt(content.lastIndexOf("、"));
        } catch (ExecutionException | InterruptedException e) {
            content = new StringBuilder("文章添加失败！");
            log.error("-----> 获取有道云笔记信息失败！", e);
        }
        return TextMessageVO.builder().toUserName(msgVO.getFromUserName()).fromUserName(fromUser)
                .msgType(MsgType.TEXT).createTime(new Date()).content(content.toString()).build();
    }

    /**
     * 使用多线程处理文章的添加以及入库
     */
    private class AddArticleThread implements Callable<String> {
        private String url;
        private CountDownLatch latch;

        AddArticleThread(String url, CountDownLatch latch) {
            this.url = url;
            this.latch = latch;
        }

        @Override
        public String call() throws Exception {
            Matcher matcher = articleIdPattern.matcher(url);
            String articleId = matcher.find() ? matcher.group(1) : "";
            try {
                String result = OkHttp3Utils.doGet(infoBaseUrl.replace("{}", articleId));
                JSONObject response = JSONObject.parseObject(result);
                String title = response.getString("tl");
                String text = Jsoup.parse(response.getString("content")).text();
                Articles article = Articles.builder().createTime(new Date()).title(title)
                        .content(text).url(url).articleId(articleId).build();
                articlesDAO.save(article);
                return title;
            } catch (IOException e) {
                log.warn("-----> 获取有道云笔记信息失败！url = {}", url);
                log.error("URL请求错误!", e);
            } finally {
                latch.countDown();
            }
            return null;
        }
    }
}
