package com.zereao.wechat.service.command;

import com.zereao.wechat.common.annotation.Command;
import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.annotation.Command.TargetSource;
import com.zereao.wechat.common.annotation.Operate;
import com.zereao.wechat.common.utils.OkHttp3Utils;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.command.toys.Img2TxtToyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 小玩具命令Service
 *
 * @author Darion Mograine H
 * @version 2019/02/15  23:57
 */
@Slf4j
@Service
public class ToysCommandService extends AbstractCommandService {
    private final Img2TxtToyService img2TxtToyService;

    @Value("${img.msg.ready.info}")
    private String imgReadyInfo;
    @Value("${toys.img2txt.temp.result.folder}")
    private String imgOutBaseFolder;
    @Value("${toys.img2txt.temp.result.path}")
    private String imgOutBasePath;
    @Value("${toys.img2txt.result.header}")
    private String resultInfoHeader;
    @Value("${toys.img2txt.result.base.url}")
    private String resultBathUrl;

    @Autowired
    public ToysCommandService(Img2TxtToyService img2TxtToyService) {this.img2TxtToyService = img2TxtToyService;}

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

    /**
     * 图片转字符画的具体操作
     *
     * @param msgVO 包含相关信息的msgVO
     * @return 文件路径
     */
    @Operate("3-1")
    public TextMessageVO img2TextImgOperate(MessageVO msgVO) throws IOException {
        String openid = msgVO.getFromUserName();
        String curTime = String.valueOf(System.currentTimeMillis());
        String sourcePath = imgOutBasePath.replace("{openid}", openid).replace("{current}", curTime);
        InputStream stream = OkHttp3Utils.doGetStream(msgVO.getPicUrl());
        img2TxtToyService.transfer2TextImg(stream, sourcePath);
        String outFolder = imgOutBaseFolder.replace("{openid}", openid).replace("{current}", curTime);
        File[] files = new File(outFolder).listFiles();
        StringBuilder content;
        if (files == null || files.length <= 0) {
            content = new StringBuilder("");
        } else {
            content = new StringBuilder(resultInfoHeader).append("\n\n");
            for (File file : files) {
                String filename = file.getName();
                String url = resultBathUrl.replace("{openid}", openid).replace("{current}", curTime).replace("{filename}", filename);
                content.append(filename).append("：\n").append(url);
            }
        }
        content.append(commonCmd);
        return TextMessageVO.builder().content(content.toString()).toUserName(openid).build();
    }
}
