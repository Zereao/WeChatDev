package com.zereao.wechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Darion Mograine H
 * @version 2019/03/22  01:41
 */
@Data
@Configuration
@ConfigurationProperties("almanac")
public class AlmanacConfig {
    private String url;
    private String luckyImgUrl;
    private String restImgUrl;
    private String errorInfo;
}
