package com.zereao.wechat.service.test;

import com.zereao.wechat.dao.UserDAO;
import com.zereao.wechat.pojo.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author Darion Mograine H
 * @version 2019/03/23  14:03
 */
@Service
public class MySqlAndRedisTransService {
    private final UserDAO userDAO;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public MySqlAndRedisTransService(UserDAO userDAO, StringRedisTemplate redisTemplate) {
        this.userDAO = userDAO;
        this.redisTemplate = redisTemplate;
    }

    @Transactional(rollbackOn = Exception.class)
    public void testTrans1() {
        User user = User.builder().username("测试事务1").openid("1231fsfas").deleteFlag(0).createTime(new Date()).build();
        userDAO.save(user);
        redisTemplate.opsForValue().set("事务测试1", "詹三事务测试");
    }

    @Transactional(rollbackOn = Exception.class)
    public void testTrans2() {
        User user = User.builder().username("测试事务2").openid("1231fsfas").deleteFlag(0).createTime(new Date()).build();
        userDAO.save(user);
        redisTemplate.opsForValue().set("事务测试2", "詹三事务测试");
        throw new RuntimeException("测试事务");
    }

    @Transactional(rollbackOn = Exception.class)
    public void testTrans3() {
        User user = User.builder().username("测试事务3").openid("1231fsfas").deleteFlag(0).createTime(new Date()).build();
        userDAO.save(user);
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.opsForValue().set("事务测试3", "詹三事务测试");
        throw new RuntimeException("测试事务");
    }

    @Transactional(rollbackOn = Exception.class)
    public void testTrans4() {
        User user = User.builder().username("测试事务4").openid("1231fsfas").deleteFlag(0).createTime(new Date()).build();
        userDAO.save(user);
        redisTemplate.opsForValue().set("事务测试4", "詹三事务测试");
        throw new RuntimeException("测试事务");
    }
}
