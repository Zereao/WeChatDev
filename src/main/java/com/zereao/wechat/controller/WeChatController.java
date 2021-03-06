package com.zereao.wechat.controller;

import com.zereao.wechat.WechatApplication;
import com.zereao.wechat.common.annotation.resolver.AnnotationResolver;
import com.zereao.wechat.common.constant.ReturnCode;
import com.zereao.wechat.common.utils.ThreadPoolUtils;
import com.zereao.wechat.pojo.vo.ApiTestVO;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.service.factory.MsgFactory;
import com.zereao.wechat.service.message.HelpMessageService;
import com.zereao.wechat.service.test.ApiTestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author Darion Mograine H
 * @version 2018/11/03  18:13
 */
@Slf4j
@RestController
// 小伦超正经
@RequestMapping("xlczj")
public class WeChatController {

    private final ApiTestService apiTestService;
    private final MsgFactory msgFactory;
    private final HelpMessageService helpMessageService;

    @Autowired
    public WeChatController(ApiTestService apiTestService, HelpMessageService helpMessageService, MsgFactory msgFactory) {
        this.apiTestService = apiTestService;
        this.helpMessageService = helpMessageService;
        this.msgFactory = msgFactory;
    }

    @PostConstruct
    public void init() {
        ThreadPoolUtils.execute(new AnnotationResolver(WechatApplication.class));
    }

    @GetMapping(value = "wechat")
    public String apiTest(ApiTestVO apiTestVo) {
        return apiTestService.checkParams(apiTestVo);
    }

    @PostMapping(value = "wechat")
    public String parseMsg(@RequestBody MessageVO msgVO) throws InterruptedException {
        Object result;
        try {
            result = msgFactory.getInstance(msgVO).handleMsg(msgVO);
        } catch (Exception e) {
            log.error("消息处理发生了错误！", e);
            result = helpMessageService.getErrorMsg(msgVO.getFromUserName());
        }
        if (result instanceof String && StringUtils.equals(String.valueOf(result), ReturnCode.WAITING)) {
            log.info("msgId = {} 的消息暂未处理完毕，等待腾讯下一次请求....", msgVO.getMsgId());
            // 不回复任何东西，直接抛异常
            TimeUnit.SECONDS.sleep(15);
        }
        StringWriter sw = new StringWriter();
        JAXB.marshal(result, sw);
        return sw.toString();
    }
}
