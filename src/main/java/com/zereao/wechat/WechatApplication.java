package com.zereao.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.zereao.wechat"})
@PropertySource(value = {"classpath:config/wechat.properties", "classpath:config/welcome_msg.properties"}, encoding = "utf-8")
public class WechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }
}
