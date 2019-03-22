package com.zereao.wechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Darion Mograine H
 * @version 2019/03/22  01:40
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "article")
public class ArticleConfig {
    private String imgBaseUrl;
    private String addInfo;
    private String youdaoUrl;
}
