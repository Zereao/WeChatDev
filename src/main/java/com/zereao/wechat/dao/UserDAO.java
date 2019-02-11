package com.zereao.wechat.dao;

import com.zereao.wechat.pojo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Darion Mograine H
 * @version 2018/12/14  14:43
 */
@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    /**
     * 根据 openId 查询唯一的用户
     *
     * @param openId     用户的OpenID
     * @param deleteFlag 删除标识
     * @return 查到的用户
     */
    User findUserByOpenidAndDeleteFlag(String openId, Integer deleteFlag);
}
