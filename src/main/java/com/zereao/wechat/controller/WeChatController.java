package com.zereao.wechat.controller;

import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.data.vo.ParentMessageVO;
import com.zereao.wechat.data.vo.test.ApiTestVO;
import com.zereao.wechat.service.factory.AbstractMsgService;
import com.zereao.wechat.service.factory.EventFactory;
import com.zereao.wechat.service.factory.MessageFactory;
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
// 小伦超正经
@RequestMapping("xlczj")
public class WeChatController {

    private final ApiTestService apiTestService;
    private final MessageFactory messageFactory;
    private final EventFactory eventFactory;

    @Autowired
    public WeChatController(ApiTestService apiTestService, MessageFactory messageFactory, EventFactory eventFactory) {
        this.apiTestService = apiTestService;
        this.messageFactory = messageFactory;
        this.eventFactory = eventFactory;
    }

    @GetMapping(value = "wechat")
    public String apiTest(ApiTestVO apiTestVo) {
        return apiTestService.checkParams(apiTestVo);
    }

    @PostMapping(value = "wechat")
    public String parseMsg(@RequestBody ParentMessageVO messageVO) {
        AbstractMsgService msgService = null;
        if (MsgType.EVENT.equals(messageVO.getMsgType())) {
            msgService = eventFactory.getInstance(messageVO);
        } else {
            msgService = messageFactory.getInstance(messageVO);
        }
        return msgService.handleMsg(messageVO);
    }
}
