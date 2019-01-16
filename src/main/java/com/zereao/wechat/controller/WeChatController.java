package com.zereao.wechat.controller;

import com.zereao.wechat.pojo.vo.ApiTestVO;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.command.HelpCommandService;
import com.zereao.wechat.service.factory.MsgFactory;
import com.zereao.wechat.service.test.ApiTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXB;
import java.io.StringWriter;

/**
 * @author Zereao
 * @version 2018/11/03  18:13
 */
@Slf4j
@RestController
// 小伦超正经
@RequestMapping("xlczj")
public class WeChatController {

    private final ApiTestService apiTestService;
    private final MsgFactory msgFactory;
    private final HelpCommandService helpCommandService;

    @Autowired
    public WeChatController(ApiTestService apiTestService, HelpCommandService helpCommandService, MsgFactory msgFactory) {
        this.apiTestService = apiTestService;
        this.helpCommandService = helpCommandService;
        this.msgFactory = msgFactory;
    }

    @GetMapping(value = "wechat")
    public String apiTest(ApiTestVO apiTestVo) {
        return apiTestService.checkParams(apiTestVo);
    }

    @PostMapping(value = "wechat")
    public String parseMsg(@RequestBody MessageVO msgVO) {
        Object result;
        try {
            result = msgFactory.getInstance(msgVO).handleMsg(msgVO);
        } catch (Exception e) {
            result = helpCommandService.getErrorMsg(msgVO.getFromUserName());
        }
        StringWriter sw = new StringWriter();
        JAXB.marshal(result, sw);
        return sw.toString();
    }
}
