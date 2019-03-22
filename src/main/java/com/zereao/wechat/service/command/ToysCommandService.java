package com.zereao.wechat.service.command;

import com.zereao.wechat.common.annotation.Command;
import com.zereao.wechat.common.annotation.Command.Level;
import com.zereao.wechat.common.annotation.Command.TargetSource;
import com.zereao.wechat.common.annotation.Operate;
import com.zereao.wechat.common.config.CommonConfig;
import com.zereao.wechat.common.config.ToysConfig;
import com.zereao.wechat.common.utils.OkHttp3Utils;
import com.zereao.wechat.common.utils.ThreadPoolUtils;
import com.zereao.wechat.pojo.vo.MessageVO;
import com.zereao.wechat.pojo.vo.TextMessageVO;
import com.zereao.wechat.service.command.toys.Img2TxtToyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 小玩具命令Service
 *
 * @author Darion Mograine H
 * @version 2019/02/15  23:57
 */
@Slf4j
@Service
@RefreshScope
public class ToysCommandService extends AbstractCommandService {
    private final Img2TxtToyService img2TxtToyService;

    private String imgReadyInfo;
    private String imgOutTempPath;
    private String resultInfoHeader;
    private String resultBaseUrl;


    @Autowired
    public ToysCommandService(Img2TxtToyService img2TxtToyService, ToysConfig toysConfig, CommonConfig commonConfig) {
        this.img2TxtToyService = img2TxtToyService;
        ToysConfig.Img2txt img2txtConfig = toysConfig.getImg2txt();
        this.imgOutTempPath = img2txtConfig.getTempPath();
        this.resultInfoHeader = img2txtConfig.getResultInfoHeader();
        this.resultBaseUrl = img2txtConfig.getResultBaseUrl();
        this.imgReadyInfo = commonConfig.getImg().getReadyInfo();
    }

    @Command(mapping = "3", name = "胖妹的玩具", level = Level.L1)
    public TextMessageVO getDarlingToys(MessageVO msgVO) {
        return this.getMenu(msgVO, Level.L2);
    }

    @Command(mapping = "3-1", name = "静态图片转字符画", level = Level.L2, src = TargetSource.IMAGE)
    public TextMessageVO img2TextImg(MessageVO msgVO) {
        return this.getResult(msgVO.getFromUserName());
    }

    /**
     * 图片转字符画的具体操作
     *
     * @param msgVO 包含相关信息的msgVO
     * @return 文件路径
     */
    @Operate("3-1")
    @SuppressWarnings("Duplicates")
    public TextMessageVO img2TextImgOperate(MessageVO msgVO) throws IOException, ExecutionException, InterruptedException {
        String openid = msgVO.getFromUserName();
        String curTime = String.valueOf(System.currentTimeMillis());
        String openidCut = openid.substring(openid.length() / 4, openid.length() / 2);
        String sourcePath = imgOutTempPath.replace("{openid}", openidCut).replace("{current}", curTime);
        InputStream stream = OkHttp3Utils.doGetStream(msgVO.getPicUrl());
        List<Map<String, String>> imgNameList = img2TxtToyService.transfer2TextImg(stream, sourcePath);
        StringBuilder content = new StringBuilder(resultInfoHeader);
        for (Map<String, String> imgInfo : imgNameList) {
            String url = resultBaseUrl.replace("{openid}", openidCut).replace("{current}", curTime).replace("{filename}", imgInfo.get("img_name"));
            content.append("\n\n").append("字体大小：").append(imgInfo.get("font_size")).append("，缩放倍数：").append(imgInfo.get("zoom")).append("：\n").append(url);
        }
        content.append(commonCmd);
        ThreadPoolUtils.execute(new GcManager(sourcePath));
        return TextMessageVO.builder().content(content.toString()).toUserName(openid).build();
    }

    @Command(mapping = "3-2", name = "动态GIF转字符画", level = Level.L2, src = TargetSource.IMAGE)
    public TextMessageVO gif2TextGif(MessageVO msgVO) {
        return this.getResult(msgVO.getFromUserName());
    }

    /**
     * GIF转字符画的具体操作
     *
     * @param msgVO 包含相关信息的msgVO
     * @return 文件URL
     */
    @Operate("3-2")
    @SuppressWarnings("Duplicates")
    public TextMessageVO gif2TextGifOperate(MessageVO msgVO) throws IOException, ExecutionException, InterruptedException {
        String openid = msgVO.getFromUserName();
        String curTime = String.valueOf(System.currentTimeMillis());
        String openidCut = openid.substring(openid.length() / 4, openid.length() / 2);
        String outPath = imgOutTempPath.replace("{openid}", openidCut).replace("{current}", curTime);
        InputStream stream = OkHttp3Utils.doGetStream(msgVO.getPicUrl());
        Map<String, String> gifInfo = img2TxtToyService.transfer2TextGif(stream, outPath);
        StringBuilder content = new StringBuilder(resultInfoHeader);
        String url = resultBaseUrl.replace("{openid}", openidCut).replace("{current}", curTime).replace("{filename}", gifInfo.get("img_name"));
        content.append("\n\n").append("字体大小：").append(gifInfo.get("font_size")).append("，缩放倍数：")
                .append(gifInfo.get("zoom")).append("：\n").append(url).append(commonCmd);
        ThreadPoolUtils.execute(new GcManager(outPath));
        return TextMessageVO.builder().content(content.toString()).toUserName(openid).build();
    }

    /**
     * 垃圾清理定时任务
     */
    private static class GcManager implements Runnable {
        private File folder;

        GcManager(String path) {
            this.folder = new File(path).getParentFile();
        }

        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(30L);
                FileUtils.deleteDirectory(folder);
                log.info("--------> 垃圾清理完毕！ path = {}", this.folder.getAbsolutePath());
            } catch (InterruptedException | IOException e) {
                log.error("-------> 临时文件夹清理失败！    filePath = {}", this.folder.getAbsolutePath());
            }
        }
    }

    /**
     * 剥离出的公共方法，用来获取准备结果
     *
     * @param openid 用户的openid
     * @return 提示信息
     */
    private TextMessageVO getResult(String openid) {
        this.imgReady(openid);
        return TextMessageVO.builder().content(imgReadyInfo + commonCmd).toUserName(openid).build();
    }
}
