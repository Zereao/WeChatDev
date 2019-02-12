package com.zereao.wechat;

import com.zereao.wechat.commom.annotation.resolver.CommandResolver;
import com.zereao.wechat.commom.utils.ThreadPoolUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

//@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.zereao.wechat"})
@PropertySource(value = {
        "classpath:config/wechat.properties",
        "classpath:config/constant-msg.properties",
        "classpath:config/youdao.properties"}, encoding = "utf-8")
public class WechatApplication {
    public static void main(String[] args) {
        ThreadPoolUtils.execute(new CommandResolver(WechatApplication.class));
        SpringApplication.run(WechatApplication.class, args);
    }
}
