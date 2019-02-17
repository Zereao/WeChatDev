package com.zereao.wechat.service.command;

import com.zereao.wechat.common.annotation.Command;
import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.annotation.Command.TargetSource;
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

    @Value("${img.msg.ready.info}")
    private String imgReadyInfo;

    @Command(mapping = "3", name = "胖妹的玩具", level = Level.L1)
    public TextMessageVO getDarlingToys(MessageVO msgVO) {
        return this.getMenu(msgVO);
    }

    @Command(mapping = "3-1", name = "图片转字符画", level = Level.L2, src = TargetSource.IMAGE)
    public TextMessageVO img2TextImg(MessageVO msgVO) {
        String openid = msgVO.getFromUserName();
        this.imgReady(openid);
        return TextMessageVO.builder().content(imgReadyInfo + commonCmd).toUserName(openid).build();
    }

}
