package com.zereao.wechat.controller;

import com.zereao.wechat.common.config.CommonConfig;
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
public class TestController {
    @Autowired
    private CommonConfig commonConfig;

    @GetMapping("/test1")
    public String test1() {
        return commonConfig.toString();
    }
}
