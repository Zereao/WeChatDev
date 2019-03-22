package com.zereao.wechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Darion Mograine H
 * @version 2019/03/22  01:31
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "common")
public class CommonConfig {
    private String ecsHost;
    private String tempPath;
    private String fromOpenid;
    private Welcome welcome;
    private Help help;
    private Menu menu;
    private Img img;

    /**
     * 用户订阅公众号后，触发的自动回复中的信息
     */
    @Data
    public static class Welcome {
        private String title;
        private String banner;
        private String description;
        private String url;
    }

    /**
     * 帮助信息
     */
    @Data
    public static class Help {
        private String errorMsg;
        private String commonMsg;
        private String rootMsg;
        private String permissionErrorMsg;
    }

    /**
     * 菜单提示信息
     */
    @Data
    public static class Menu {
        private String headerInfo;
        private String commonCmd;
    }

    /**
     * 图片消息，提示信息
     */
    @Data
    public static class Img {
        private String readyInfo;
        private String errorMsg;
    }
}
