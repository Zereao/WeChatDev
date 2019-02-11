package com.zereao.wechat.dao;

import com.zereao.wechat.pojo.po.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Darion Mograine H
 * @version 2018/12/13  21:27
 */
@Repository
public interface MessageDAO extends JpaRepository<Message, Integer> {

}
