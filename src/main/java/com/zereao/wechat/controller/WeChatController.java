package com.zereao.wechat.controller;

import com.zereao.wechat.data.vo.message.ParentMessageVO;
import com.zereao.wechat.data.vo.test.ApiTestVO;
import com.zereao.wechat.service.test.ApiTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zereao
 * @version 2018/11/03  18:13
 */
@Slf4j
@RestController
@RequestMapping("xlczj")
public class WeChatController {

    private final ApiTestService apiTestService;

    @Autowired
    public WeChatController(ApiTestService apiTestService) {
        this.apiTestService = apiTestService;
    }

    @GetMapping(value = "wechat")
    public String apiTest(ApiTestVO apiTestVo) {
        return apiTestService.checkParams(apiTestVo);
    }

    @PostMapping(value = "wechat")
    public String parseMsg(@RequestBody ParentMessageVO messageVO) {
        log.info(messageVO.toString());
        return null;
    }
}
