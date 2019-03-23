package com.zereao.wechat.controller;

import com.zereao.wechat.common.config.*;
import com.zereao.wechat.service.test.MySqlAndRedisTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Darion Mograine H
 * @version 2019/03/22  10:14
 */
@RestController
@RequestMapping
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
public class TestController {
    @Autowired
    private MySqlAndRedisTransService service;

    @GetMapping("/test")
    public String test() {
        return commonConfig.toString();
    }

    @GetMapping("/test1")
    public String test1() {
        service.testTrans1();
        return "SUCCESS";
    }

    @GetMapping("/test2")
    public String test2() {
        service.testTrans2();
        return "SUCCESS";
    }

    @GetMapping("/test3")
    public String test3() {
        service.testTrans3();
        return "SUCCESS";
    }

    @GetMapping("/test4")
    public String test4() {
        service.testTrans4();
        return "SUCCESS";
    }

    @Autowired
    private AlmanacConfig almanacConfig;

    @Autowired
    private ArticleConfig articleConfig;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private ToysConfig toysConfig;

    @Autowired
    private WechatConfig wechatConfig;

    @GetMapping("/test5")
    public String test5() {
        System.out.println(almanacConfig.toString());
        System.out.println(articleConfig.toString());
        System.out.println(commonConfig.toString());
        System.out.println(toysConfig.toString());
        System.out.println(wechatConfig.toString());
        return "SUCCESS";
    }
}
