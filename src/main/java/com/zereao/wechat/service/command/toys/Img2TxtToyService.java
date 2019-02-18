package com.zereao.wechat.service.command.toys;

import com.zereao.wechat.common.utils.SpringBeanUtils;
import com.zereao.wechat.common.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

/**
 * 图片转txt
 *
 * @author Darion Mograine H
 * @version 2019/02/15  15:00
 */
@Slf4j
@Service
public class Img2TxtToyService {
    @Value("${toys.img2txt.elements}")
    private String strElements;
    @Value("${toys.img2txt.temp.result.path}")
    private String outBasePath;

    /**
     * 将源文件转换为字符画，实际转换操作为异步执行。
     * 共计生成5张字符画，字体大小分别为：6 7 8 9 10； 缩放倍率为 3 3 4 4 5
     *
     * @param source     源文件的输入流
     * @param sourcePath 源文件路径
     * @throws IOException IO异常
     */
    public List<Map<String, String>> transfer2TextImg(InputStream source, String sourcePath) throws IOException, ExecutionException, InterruptedException {
        return this.transfer2TextImg(ImageIO.read(source), sourcePath);
    }

    /**
     * 将源文件转换为字符画，实际转换操作为异步执行。
     * 共计生成5张字符画，字体大小分别为：6 7 8 9 10； 缩放倍率为 3 3 4 4 5
     *
     * @param sourcePath 源文件路径
     * @throws IOException IO异常
     */
    public List<Map<String, String>> transfer2TextImg(String sourcePath) throws IOException, ExecutionException, InterruptedException {
        return this.transfer2TextImg(ImageIO.read(new File(sourcePath)), sourcePath);
    }

    /**
     * 将源文件转换为字符画，实际转换操作为异步执行。
     * 共计生成5张字符画，字体大小分别为：6 7 8 9 10； 缩放倍率为 3 3 4 4 5
     *
     * @param img        源文件的BufferedImage对象
     * @param sourcePath 源文件路径
     */
    public List<Map<String, String>> transfer2TextImg(BufferedImage img, String sourcePath) throws ExecutionException, InterruptedException, IOException {
        int width = img.getWidth(), height = img.getHeight();
        // 如果图片的长或宽超过1000像素，将其等比压缩至最长边为1000像素
        boolean maxOver1000 = (width > height ? width : height) > 1000;
        if (maxOver1000) {
            img = Thumbnails.of(img).scale(1000, 1000).asBufferedImage();
        }
        String[][] chars = this.transfer2CharArray(img);
        List<Map<String, String>> imgNameList = new CopyOnWriteArrayList<>();
        for (int fontSize = 6; fontSize <= 10; fontSize++) {
            imgNameList.add(ThreadPoolUtils.submit(new Text2ImgTask(chars, sourcePath, fontSize)));
        }
        return imgNameList;
    }

    /**
     * 图片转字符串
     *
     * @param img BufferedImage图片
     * @return 转换后的字符串
     */
    public String transfer2String(BufferedImage img) {
        int len = strElements.length();
        int width = img.getWidth();
        int height = img.getHeight();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // (0,0),(1,0),(2,0),(3,0),(4,0),换行,(0,1),(1,1),(2,1),(3,1),(4,1) 这个顺序开始读的
                int index = this.getCharIndex(img.getRGB(j, i));
                result.append(index >= len ? " " : String.valueOf(strElements.charAt(index)));
            }
            result.append("\r\n");
        }
        return result.toString();
    }

    /**
     * 图片转字符数组，宽 x 高 = 1 x 1
     * 返回的二维数组的大小等于 Height ； 元素的大小等于 Width
     *
     * @param img BufferedImage图片
     * @return 转换后二维字符数组
     */
    public String[][] transfer2CharArray(BufferedImage img) {
        int len = strElements.length();
        int width = img.getWidth();
        int height = img.getHeight();
        String[][] result = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = this.getCharIndex(img.getRGB(j, i));
                result[i][j] = index >= len ? " " : String.valueOf(strElements.charAt(index));
            }
        }
        return result;
    }

    /**
     * @param source   需要压缩的源文件的BufferedImage对象
     * @param longSize 压缩后较长边的大小
     * @return 压缩后的BufferedImage对象
     */
    @Deprecated
    public BufferedImage compress(BufferedImage source, int longSize) {
        int type = source.getType();
        int oldWidth = source.getWidth();
        int oldHeight = source.getHeight();
        double wZoom = (double) longSize / oldWidth;
        double hZoom = (double) longSize / oldHeight;
        int width = longSize, height = longSize;
        if (wZoom < hZoom) {
            hZoom = wZoom;
            height = (int) (wZoom * oldHeight);
        } else {
            wZoom = hZoom;
            width = (int) (hZoom * oldWidth);
        }
        BufferedImage target;
        if (type == BufferedImage.TYPE_CUSTOM) {
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(width, height, type);
        }
        Graphics2D graphics2D = target.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawRenderedImage(source, AffineTransform.getScaleInstance(wZoom, hZoom));
        graphics2D.dispose();
        return target;
    }

    /**
     * 图片转字符再保存为图片
     *
     * @param chars    原图转出的二维字符数组
     * @param outPath  输出路径
     * @param fontSize 转换出的图片中的文字大小，也是缩放倍数x2 。
     *                 推荐 传入偶数 8；传入 fontSize = 8时，字体大小为8,缩放倍数为 8 / 2 =4；输出像素间隔为
     * @return 生成的文件的信息Map
     */
    public Map<String, String> textToImage(String[][] chars, String outPath, int fontSize) throws IOException {
        int zoom = fontSize / 2;
        int extIndex = outPath.lastIndexOf(".");
        StringBuilder config = new StringBuilder("_fontsize=").append(fontSize).append("_zoom=").append(zoom);
        String realOutPath = new StringBuilder(outPath).insert(extIndex, config).toString();
        File outImg = new File(realOutPath);
        if (!outImg.exists()) {
            boolean newFile = outImg.mkdirs();
            newFile = outImg.createNewFile();
        }
        int width = chars[0].length * zoom;
        int height = chars.length * zoom;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图像上下文
        Graphics graphics = createGraphics(bufferedImage, width, height, fontSize);
        for (int i = 0, lenOfH = chars.length; i < lenOfH; i += 2) {
            for (int j = 0, lenOfW = chars[i].length; j < lenOfW; j += 2) {
                graphics.drawString(chars[i][j], j * zoom, i * zoom);
            }
        }
        graphics.dispose();
        // 保存为jpg图片
        boolean result = ImageIO.write(bufferedImage, outPath.substring(extIndex + 1), outImg);
        String imgName = outImg.getName();
        log.info("--------> 文本转图片{}！   img = {}", result ? "成功" : "失败", imgName);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("font_size", String.valueOf(fontSize));
        resultMap.put("zoom", String.valueOf(zoom));
        resultMap.put("img_name", imgName);
        return resultMap;
    }

    /**
     * 将 二维字符数组按照格式写入txt文件中
     *
     * @param content 二维字符数组内容
     * @param outPath 输出路径
     * @throws IOException IO异常
     */
    public void write2File(String[][] content, String outPath) throws IOException {
        File file = new File(outPath);
        if (file.isFile() && file.exists()) {
            boolean del = file.delete();
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (String[] strings : content) {
                for (String string : strings) {
                    writer.append(string);
                }
                writer.append("\r\n");
                writer.flush();
            }
        }
    }

    private static class Text2ImgTask implements Callable<Map<String, String>> {
        private String[][] chars;
        private String outPath;
        private int fontSize;

        Text2ImgTask(String[][] chars, String outPath, int fontSize) {
            this.chars = chars;
            this.outPath = outPath;
            this.fontSize = fontSize;
        }

        @Override
        public Map<String, String> call() throws Exception {
            return SpringBeanUtils.getBean(Img2TxtToyService.class).textToImage(chars, outPath, fontSize);
        }
    }

    /**
     * 提取的公共方法，根据像素值pixel获取该像素应该填充的字符元素的索引
     *
     * @param pixel 像素值
     * @return 填充元素的索引
     */
    private int getCharIndex(int pixel) {
        // 下面三行代码将一个数字转换为RGB数字
        int red = (pixel & 0xff0000) >> 16;
        int green = (pixel & 0xff00) >> 8;
        int blue = (pixel & 0xff);
        float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
        return Math.round(gray * (strElements.length() + 1) / 255);
    }

    /**
     * 画板默认一些参数设置
     *
     * @param image    图片
     * @param width    图片宽
     * @param height   图片高
     * @param fontSize 字体大小
     * @return 创建的Graphics对象
     */
    private static Graphics createGraphics(BufferedImage image, int width, int height, int fontSize) {
        Graphics graphics = image.createGraphics();
        // 设置背景色
        graphics.setColor(null);
        // 绘制背景
        graphics.fillRect(0, 0, width, height);
        // 设置前景色
        graphics.setColor(Color.BLACK);
        // 设置字体
        graphics.setFont(new Font("宋体", Font.PLAIN, fontSize));
        return graphics;
    }
}
