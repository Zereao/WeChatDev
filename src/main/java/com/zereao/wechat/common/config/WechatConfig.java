package com.zereao.wechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Darion Mograine H
 * @version 2019/03/22  01:38
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {
    private String token;
    private String baseUrl;
    private String appid;
    private String secret;
    private String accessTokenUrl;
    private String ipUrl;
}
