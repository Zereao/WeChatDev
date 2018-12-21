package com.zereao.wechat.controller;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.vo.MessageVO;
import com.zereao.wechat.data.vo.ApiTestVO;
import com.zereao.wechat.service.command.HelpCommandService;
import com.zereao.wechat.service.factory.AbstractMsgService;
import com.zereao.wechat.service.factory.EventFactory;
import com.zereao.wechat.service.factory.MessageFactory;
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
    private final MessageFactory messageFactory;
    private final EventFactory eventFactory;
    private final HelpCommandService helpCommandService;

    @Autowired
    public WeChatController(ApiTestService apiTestService, MessageFactory messageFactory, EventFactory eventFactory, HelpCommandService helpCommandService) {
        this.apiTestService = apiTestService;
        this.messageFactory = messageFactory;
        this.eventFactory = eventFactory;
        this.helpCommandService = helpCommandService;
    }

    @GetMapping(value = "wechat")
    public String apiTest(ApiTestVO apiTestVo) {
        return apiTestService.checkParams(apiTestVo);
    }

    @PostMapping(value = "wechat")
    public String parseMsg(@RequestBody MessageVO msgVO) {
        AbstractMsgService msgService = MsgType.EVENT.equals(msgVO.getMsgType()) ?
                eventFactory.getInstance(msgVO) : messageFactory.getInstance(msgVO);
        Object result;
        try {
            result = msgService.handleMsg(msgVO);
        } catch (Exception e) {
            result = helpCommandService.getErrorMsg(msgVO.getFromUserName());
        }
        StringWriter sw = new StringWriter();
        JAXB.marshal(result, sw);
        return sw.toString();
    }
}
