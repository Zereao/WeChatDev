package com.zereao.wechat.controller.test;

import com.zereao.wechat.data.vo.ApiTestVo;
import com.zereao.wechat.service.test.ApiTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zereao
 * @version 2018/11/03  18:13
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    private final ApiTestService apiTestService;

    @Autowired
    public TestController(ApiTestService apiTestService) {
        this.apiTestService = apiTestService;
    }

    @RequestMapping(value = "/apiTest")
    public String apiTest(ApiTestVo apiTestVo) {
        return apiTestService.checkParams(apiTestVo);
    }
}