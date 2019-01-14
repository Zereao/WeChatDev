package com.zereao.wechat.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * 回复的消息，存在MongoDB中
 *
 * @author Zereao
 * @version 2018/12/18  10:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("articles")
public class Articles {
    /**
     * 主键ID
     */
    @Id
    private String id;
    /**
     * 文章ID
     */
    private String articleId;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章地址
     */
    private String url;
    /**
     * 消息创建时间 （整型）
     */
    private Date createTime;
}
