package com.zereao.wechat;

import com.zereao.wechat.commom.annotation.resolver.CommandResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.zereao.wechat"})
@PropertySource(value = {
        "classpath:config/wechat.properties",
        "classpath:config/constant-msg.properties",
        "classpath:config/youdao.properties"}, encoding = "utf-8")
public class WechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
        CommandResolver.run(WechatApplication.class);
    }
}
