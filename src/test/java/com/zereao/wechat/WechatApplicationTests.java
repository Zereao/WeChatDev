package com.zereao.wechat;

import com.zereao.wechat.service.command.toys.Img2TxtToyService;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
class WechatApplicationTests {
    @Autowired
    private Img2TxtToyService toyService;

    @Test
    void contextLoads() throws IOException, ExecutionException, InterruptedException {
        String path = "/Users/jupiter/Documents/微信图片_20190215122021.jpg";
        String outPath = "/Users/jupiter/Documents/转.txt";
        File file = new File(path);
        BufferedImage img = Thumbnails.of(file).size(100, 100).asBufferedImage();
        String[][] chars = toyService.transfer2CharArray(img);
        File outFile = new File(outPath);
        if (outFile.isFile() && outFile.exists()) {
            boolean del = outFile.delete();
        }
        try (FileWriter writer = new FileWriter(outFile)) {
            for (int i = 0, len1 = chars.length; i < len1; i++) {
                for (int j = 0, len2 = chars[0].length; j < len2; j++) {
                    writer.append(chars[i][j]);
                }
                writer.append("\r\n");
                writer.flush();
            }
        }
    }

}
