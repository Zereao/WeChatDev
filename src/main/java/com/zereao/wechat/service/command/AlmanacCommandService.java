package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Darion Mograine H
 * @version 2019/02/12  15:58
 */
@Service
public class AlmanacCommandService extends AbstractCommandService {

    @Command(mapping = "2", name = "老爹的黄历", first = true, menu = Command.MenuType.USER)
    public TextMessageVO getFatherAlmanac(MessageVO msgVO) {
        return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                .toUserName(msgVO.getFromUserName()).content("测试收到啦").build();
    }

    /*

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
            content.append("\n\n为了缓解服务器压力，文章代码5分钟内有效哦~").append(commonCmd);
        }
        return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                .toUserName(msgVO.getFromUserName()).content(content.toString()).build();
    }

     public Object getArticle(MessageVO msgVO) {
        String toUser = msgVO.getFromUserName();
        Map<Object, Object> articleMap = redisService.hmget(toUser.concat(redisKeySuffix));
        if (articleMap == null || articleMap.size() <= 0) {
            return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                    .toUserName(toUser).content("文章列表缓存已过期，请重新操作~" + commonCmd).build();
        }
        String articleId = String.valueOf(articleMap.get(msgVO.getContent().replace("1-*", "")));
        Articles article;
        if (StringUtils.isEmpty(articleId) || (article = articlesDAO.findById(articleId).orElse(null)) == null) {
            return TextMessageVO.builder().createTime(new Date()).msgType(MsgType.TEXT).fromUserName(fromUser)
                    .toUserName(toUser).content("文章不存在哦~请检查您发送的代码是否正确~" + commonCmd).build();
        }
        String picUrl = imgUrl.replace("{}", String.valueOf(RandomUtils.nextInt(1, 13)));
        NewsMessageVO.Articles.Item item = NewsMessageVO.Articles.Item.builder().title(article.getTitle()).picUrl(picUrl)
                .url(article.getUrl()).description(article.getContent().substring(0, 37).concat("....\n\n查看全文")).build();
        NewsMessageVO.Articles articles = NewsMessageVO.Articles.builder().item(item).build();
        return NewsMessageVO.builder().articles(articles).msgType(MsgType.NEWS).toUserName(toUser).fromUserName(fromUser)
                .articleCount(1).createTime(new Date()).build();
    }
    * */
}
