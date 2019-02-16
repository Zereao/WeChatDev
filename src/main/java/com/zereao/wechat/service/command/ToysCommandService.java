package com.zereao.wechat.service.command;

import com.zereao.wechat.commom.annotation.Command;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 小玩具命令Service
 *
 * @author Darion Mograine H
 * @version 2019/02/15  23:57
 */
@Slf4j
@Service
public class ToysCommandService extends AbstractCommandService {

    @Value("${toys.txt2img.header}")
    private String txt2ImgHeader;

    @Command(mapping = "3", name = "胖妹的玩具", level = Command.Level.L1)
    public TextMessageVO getDarlingToys(MessageVO msgVO) {
        return this.getMenu(msgVO);
    }

    @Command(mapping = "3-1", name = "图片转字符画", level = Command.Level.L2)
    public TextMessageVO img2TextImg(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();

        return TextMessageVO.builder().content(txt2ImgHeader + commonCmd).toUserName(openid).build();
    }

}
