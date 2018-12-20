package com.zereao.wechat.service.message;

import com.zereao.wechat.commom.constant.Command;
import com.zereao.wechat.data.vo.ParentMsgVO;
import com.zereao.wechat.service.command.AbstractCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 处理接收到的文本消息的Service
 *
 * @author Zereao
 * @version 2018/12/11  20:03
 */
@Slf4j
@Service
public class TextMessageService extends AbstractMessageService {
    private final Map<String, AbstractCommandService> commandServiceMap;

    @Autowired
    public TextMessageService(Map<String, AbstractCommandService> commandServiceMap) {this.commandServiceMap = commandServiceMap;}

    @Override
    public Object handleMessage(ParentMsgVO msgVO) {
        /* 胖砸，想不到吧！*/
        return Command.of(msgVO.getContent().split("\\[wdxpn]|\\[WDXPN]")[0]).exec(commandServiceMap, msgVO);
    }
}
