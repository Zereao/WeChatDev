package com.zereao.wechat.dao;

import com.zereao.wechat.data.bo.ResponseMsg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zereao
 * @version 2018/12/18  10:52
 */
@Repository
public interface ResponseMsgDAO extends MongoRepository<ResponseMsg, Integer> {

}
