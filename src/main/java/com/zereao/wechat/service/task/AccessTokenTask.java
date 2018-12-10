package com.zereao.wechat.service.task;

import com.zereao.wechat.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 获取AccessToken 定时任务
 *
 * @author Zereao
 * @version 2018/12/10  16:32
 */
@Slf4j
@Service

public class AccessTokenTask {
    @Value("wechat.access_token.url")
    private String accessTokenUrl;

    public static final String GLOBAL_TOKEN = "WECHAT_REDIS_GLOBAL_TOKEN_KEY";

    private final RedisService redisService;

    @Autowired
    public AccessTokenTask(RedisService redisService) {this.redisService = redisService;}

    @Scheduled(fixedRate = 3000)

    public void accessTokenTask() {
        log.info("------>  准备获取Token");
//        redisService.set(GLOBAL_TOKEN, )

    }

}
