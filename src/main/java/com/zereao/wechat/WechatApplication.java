package com.zereao.wechat;

import com.zereao.wechat.common.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.zereao.wechat"})
@EnableConfigurationProperties({CommonConfig.class, ToysConfig.class,
        WechatConfig.class, ArticleConfig.class, AlmanacConfig.class})
public class WechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }
}