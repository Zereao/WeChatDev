package com.zereao.wechat.service.task;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.common.utils.OkHttp3Utils;
import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 获取AccessToken 定时任务
 *
 * @author Zereao
 * @version 2018/12/10  16:32
 */
@Slf4j
@Service
public class AccessTokenTask {
    @Value("${wechat.access_token.url}")
    private String accessTokenUrl;

    public static final String GLOBAL_TOKEN = "WECHAT_REDIS_GLOBAL_TOKEN_KEY";

    private final RedisService redisService;

    @Autowired
    public AccessTokenTask(RedisService redisService) {this.redisService = redisService;}

    // 设置为每 1.5 小时获取一次。 1.5 * 60 * 60 * 1000 = 5400000
//    @Scheduled(fixedRate = 5400000)
    public void accessTokenTask() throws IOException {
        log.info("------>  准备获取Token");
        String result = OkHttp3Utils.doGet(accessTokenUrl);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (resultJson.containsKey("access_token")) {
            log.info(resultJson.toString());
            // 将拿到的access_token放入redis，并设置两小时过期
            redisService.set(GLOBAL_TOKEN, resultJson.getString("access_token"), 2 * 60 * 60);
        } else {
            log.info("-----> 获取 access_token 出现异常：{}", result);
        }
    }
}
