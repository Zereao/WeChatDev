package com.zereao.wechat.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Darion Mograine H
 * @version 2019/03/21  23:31
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "toys")
public class ToysConfig {
    private Img2txt img2txt;

    /**
     * 图片转字符画配置类
     */
    @Data
    public static class Img2txt {
        /**
         * 填充图案的元素，默认为 @#&$%*o!;
         */
        private String elements = "@#&$%*o!; ";
        /**
         * 生成的临时文件路径
         */
        private String tempPath;
        /**
         * 返回信息的头信息
         */
        private String resultInfoHeader;
        /**
         * 返回结果图片地址的BaseUrl
         */
        private String resultBaseUrl;
    }
}
