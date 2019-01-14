package com.zereao.wechat.service.command;

import com.alibaba.fastjson.JSONObject;
import com.zereao.wechat.commom.constant.MsgType;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Root命令Service
 *
 * @author Zereao
 * @version 2018/12/21  16:01
 */
@Slf4j
@Service
public class RootCommandService extends AbstractCommandService {
    private final HelpCommandService helpCommandService;

    @Autowired
    public RootCommandService(HelpCommandService helpCommandService) {this.helpCommandService = helpCommandService;}

    @Value("${wechat.from.openid}")
    private String fromUser;
    @Value("classpath:data/commands-root.json")
    private Resource resource;

    public TextMessageVO showRootCommands(MessageVO msgVO) {
        try {
            JSONObject commandsJson = JSONObject.parseObject(resource.getInputStream(), Charset.forName("utf-8"), JSONObject.class);
            StringBuilder content = new StringBuilder();
            commandsJson.getJSONObject("command").forEach((key, value) -> content.append(key).append("：").append(value).append("\n"));
            content.append(commandsJson.getString("ps"));
            return TextMessageVO.builder().content(content.toString()).createTime(new Date()).fromUserName(fromUser)
                    .msgType(MsgType.TEXT).toUserName(msgVO.getFromUserName()).build();
        } catch (IOException e) {
            log.error("读取commands.json 文件失败！", e);
        }
        return helpCommandService.getErrorMsg(msgVO.getFromUserName());
    }

}
