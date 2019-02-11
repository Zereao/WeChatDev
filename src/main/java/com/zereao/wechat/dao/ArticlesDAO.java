package com.zereao.wechat.dao;

import com.zereao.wechat.pojo.po.Articles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Darion Mograine H
 * @version 2018/12/20  13:41
 */
@Repository
public interface ArticlesDAO extends MongoRepository<Articles, String> {

}
