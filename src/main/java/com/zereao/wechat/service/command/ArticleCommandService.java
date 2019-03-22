package com.zereao.wechat.service.command;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.common.annotation.Command;
import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.annotation.Command.MenuType;
import com.zereao.wechat.common.config.ArticleConfig;
import com.zereao.wechat.common.utils.OkHttp3Utils;
import com.zereao.wechat.common.utils.ThreadPoolUtils;
import com.zereao.wechat.dao.ArticlesDAO;
import com.zereao.wechat.pojo.po.Articles;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.NewsMessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 伦哥的随笔
 *
 * @author Darion Mograine H
 * @version 2018/12/19  20:33
 */
@Slf4j
@Service
@RefreshScope
public class ArticleCommandService extends AbstractCommandService {
    private final ArticlesDAO articlesDAO;

    private static final Pattern ARTICLE_ID_PATTERN = Pattern.compile("id=(.*)&");

    private String infoBaseUrl;
    private String imgUrl;
    private String addInfo;

    // 文章列表 - redisKey - 后缀
    private String redisKeySuffix = "|article-list";


    @Autowired
    public ArticleCommandService(ArticlesDAO articlesDAO, ArticleConfig articleConfig) {
        this.articlesDAO = articlesDAO;
        this.infoBaseUrl = articleConfig.getYoudaoUrl();
        this.imgUrl = articleConfig.getImgBaseUrl();
        this.addInfo = articleConfig.getAddInfo();
    }

    @Command(mapping = "1", name = "伦哥的随笔", level = Level.L1)
    public TextMessageVO godEssay(MessageVO msgVO) {
        return this.getMenu(msgVO, Level.L2);
    }

    /**
     * 获取所有的文章(标题)
     *
     * @param msgVO 包含所需参数的消息体
     */
    @Command(mapping = "1-1", name = "获取文章列表", level = Level.L2)
    public TextMessageVO getAllArticles(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
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
            redisService.hmset(openid + redisKeySuffix, articleMap, 5 * 60);
            content.append("\n\n为了缓解服务器压力，文章代码5分钟内有效哦~").append(commonCmd);
        }
        return TextMessageVO.builder().toUserName(openid).content(content.toString()).build();
    }

    @Command(mapping = "1-1-*", name = "获取文章", level = Level.L0)
    public Object getArticle(MessageVO msgVO) throws NoSuchMethodException {
        String toUser = msgVO.getFromUserName();
        Map<Object, Object> articleMap = redisService.hmget(toUser.concat(redisKeySuffix));
        if (articleMap == null || articleMap.size() <= 0) {
            return TextMessageVO.builder().toUserName(toUser).content("文章列表缓存已过期，请重新操作~" + commonCmd).build();
        }
        Command thisCmd = this.getClass().getDeclaredMethod("getArticle", MessageVO.class).getAnnotation(Command.class);
        String articleId = String.valueOf(articleMap.get(msgVO.getContent().replace(thisCmd.mapping(), "")));
        Articles article;
        if (StringUtils.isEmpty(articleId) || (article = articlesDAO.findById(articleId).orElse(null)) == null) {
            return TextMessageVO.builder().toUserName(toUser).content("文章不存在哦~请检查您发送的代码是否正确~" + commonCmd).build();
        }
        String picUrl = imgUrl.replace("{}", String.valueOf(RandomUtils.nextInt(1, 13)));
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title(article.getTitle()).picUrl(picUrl)
                .url(article.getUrl()).description(article.getContent().substring(0, 37).concat("....\n\n查看全文")).build();
        return NewsMessageVO.builder().articles(new NewsMessageVO.Articles(item)).toUserName(toUser).build();
    }

    @Command(name = "新增文章", mapping = "1-r1", level = Level.L2, menu = MenuType.ROOT)
    public TextMessageVO addArticle(MessageVO msgVO) {
        return TextMessageVO.builder().toUserName(msgVO.getFromUserName()).content(addInfo).build();
    }

    @Command(name = "文章添加操作", mapping = "1-r1-*", level = Level.L0, menu = MenuType.ROOT)
    public TextMessageVO addArticleOperate(MessageVO msgVO) {
        String[] urls = msgVO.getContent().split("\\|(wdxpn|WDXPN)\\|");
        CountDownLatch latch = new CountDownLatch(urls.length);
        StringBuilder content;
        String openid = msgVO.getFromUserName();
        try {
            content = new StringBuilder("文章");
            List<Future<String>> futureList = new ArrayList<>();
            for (String url : urls) {
                futureList.add(ThreadPoolUtils.submit(new AddArticleThread(url, latch)));
            }
            int index = urls.length;
            while (index > 0) {
                Iterator<Future<String>> iter = futureList.iterator();
                //noinspection WhileLoopReplaceableByForEach
                while (iter.hasNext()) {
                    Future<String> future = iter.next();
                    if (future.isDone()) {
                        String title = future.get();
                        if (StringUtils.isEmpty(title)) {
                            log.error("------> 文章添加失败！");
                        } else {
                            content.append("【").append(title).append("】、");
                        }
                        --index;
                        futureList.remove(future);
                    }
                }
            }
            latch.await();
            content.append("添加成功！").deleteCharAt(content.lastIndexOf("、")).append(commonCmd);
        } catch (ExecutionException | InterruptedException e) {
            content = new StringBuilder("文章添加失败！").append(commonCmd);
            log.error("-----> 获取有道云笔记信息失败！", e);
        } finally {
            this.cleanCommand(openid);
        }
        return TextMessageVO.builder().toUserName(openid).content(content.toString()).build();
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
            Matcher matcher = ARTICLE_ID_PATTERN.matcher(url);
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
