package com.zereao.wechat.service.task;

import com.zereao.wechat.pojo.dto.AlmanacDTO;
import com.zereao.wechat.service.command.AlmanacCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Darion Mograine H
 * @version 2019/02/14  17:22
 */
@Slf4j
@Service
public class AlmanacTask {
    private final AlmanacCommandService commandService;

    @Autowired
    public AlmanacTask(AlmanacCommandService commandService) {this.commandService = commandService;}

    /**
     * 先将缓存失效，然后再 每天 00:00:01 时间点获取黄历，并缓存
     */
    @Scheduled(cron = "1 0 0 * * ?")
    @CacheEvict(cacheNames = "almanac", allEntries = true, beforeInvocation = true)
    public void almanacTask() {
        log.info("--------> 老爹的黄历 定时任务  开始！");
        AlmanacDTO almanacDTO = commandService.getAlmanacInfo();
        log.info("--------> 老爹的黄历 定时任务  结束！ result = {}", almanacDTO);
    }
}
