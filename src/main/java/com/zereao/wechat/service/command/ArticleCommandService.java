package com.zereao.wechat.service.command;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.commom.utils.OkHttp3Utils;
import com.zereao.wechat.commom.utils.ThreadPoolUtils;
import com.zereao.wechat.dao.ArticlesDAO;
import com.zereao.wechat.data.bo.Articles;
import com.zereao.wechat.data.vo.MessageVO;
import com.zereao.wechat.data.vo.NewsMessageVO;
import com.zereao.wechat.data.vo.TextMessageVO;
import com.zereao.wechat.service.redis.RedisService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
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

    @Autowired
    public ArticleCommandService(ArticlesDAO articlesDAO, RedisService redisService) {
        this.articlesDAO = articlesDAO;
        this.redisService = redisService;
    }

    public TextMessageVO addArticle(MessageVO msgVO) {
        String[] urls = msgVO.getContent().replaceAll("1-root\\.add\\[wdxpn]|1-root\\.add\\[WDXPN]", "").split("\\[wdxpn]|\\[WDXPN]");
        CountDownLatch latch = new CountDownLatch(urls.length);
        StringBuilder content;
        try {
            content = new StringBuilder("文章");
            for (String url : urls) {
                String title = ThreadPoolUtils.submit(new AddArticleThread(url, latch));
                content.append("【").append(title).append("】、");
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

    @Data
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
                String result = OkHttp3Utils.INSTANCE.doGet(infoBaseUrl.replace("{}", articleId));
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

    /**
     * 获取所有的文章(标题)
     *
     * @param msgVO 包含所需参数的消息体
     */
    public TextMessageVO getAllArticles(MessageVO msgVO) {
        List<Articles> articlesList = articlesDAO.findAll();
        StringBuilder content;
        if (articlesList.size() <= 0) {
            content = new StringBuilder("当前还有文章~快去撩撩伦哥让他添加吧~");
        } else {
            content = new StringBuilder("您可以回复文章标题前面的代码查看文章~\n");
            for (int i = 1, size = articlesList.size(); i < size + 1; i++) {
                // redis key 的格式， openid|index
                String redisKey = msgVO.getFromUserName() + "|" + i;
                // 将 文章ID信息放入 redis，5分钟内有效
                Articles articles = articlesList.get(i - 1);
                redisService.set(redisKey, articles.getId(), 5 * 60);
                content.append("\n").append("1-").append(i).append("：").append(articles.getTitle());
            }
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


}
