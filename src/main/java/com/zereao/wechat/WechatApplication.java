package com.zereao.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@EnableCaching
@SpringBootApplication(scanBasePackages = {"com.zereao.wechat"})
@PropertySource(value = {
        "classpath:config/wechat.properties",
        "classpath:config/constant-msg.properties",
        "classpath:config/almanac.properties",
        "classpath:config/youdao.properties"}, encoding = "utf-8")
public class WechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }
}
