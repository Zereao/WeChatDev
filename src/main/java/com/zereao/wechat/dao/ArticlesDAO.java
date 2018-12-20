package com.zereao.wechat.dao;

import com.zereao.wechat.data.bo.Articles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zereao
 * @version 2018/12/20  13:41
 */
@Repository
public interface ArticlesDAO extends MongoRepository<Articles, Integer> {

}
