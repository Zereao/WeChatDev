package com.zereao.wechat.data.bo;

import lombok.Builder;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Zereao
 * @version 2018/12/14  14:43
 */
@Entity
@Builder
@Table(name = "user")
public class User {
    /**
     * 主键，用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * openID
     */
    private String openid;
    /**
     * 删除标识，1 - 已删除；0 - 未删除
     */
    private Integer delete_flag;
    /**
     * 创建时间
     */
    private Date create_time;
    /**
     * 更新时间
     */
    private Date update_time;
}
