package com.zereao.wechat.service.command.toys;

import com.zereao.wechat.common.utils.SpringBeanUtils;
import com.zereao.wechat.common.utils.ThreadPoolUtils;
import com.zereao.wechat.common.utils.gifencoder.AnimatedGifEncoder;
import com.zereao.wechat.common.utils.gifencoder.GifDecoder;
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
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
    private String strElements = "@#&$%*o!; ";

    /**
     * 将源文件转换为字符画，实际转换操作为异步执行。
     * 共计生成5张字符画，字体大小分别为：6 7 8 9 10； 缩放倍率为 3 3 4 4 5
     *
     * @param source     源文件的输入流
     * @param sourcePath 源文件路径
     * @throws IOException IO异常
     */
    public List<Map<String, String>> transfer2TextImg(InputStream source, String sourcePath) throws IOException, ExecutionException, InterruptedException {
        BufferedImage img = ImageIO.read(source);
        // 如果图片的长或宽超过1000像素，将其等比压缩至最长边为1000像素
        if (img.getWidth() > 1000 || img.getHeight() > 1000) {
            // 等比例缩放至 最长边为1000
            img = Thumbnails.of(img).scale(1000, 1000).asBufferedImage();
        }
        String[][] chars = this.transfer2CharArray(img);
        List<Future<Map<String, String>>> futureList = new ArrayList<>();
        for (int fontSize = 6; fontSize <= 10; fontSize++) {
            futureList.add(ThreadPoolUtils.submit(new Text2ImgTask(chars, sourcePath, fontSize, fontSize / 2)));
        }
        List<Map<String, String>> imgNameList = new ArrayList<>();
        int index = 5;
        while (index > 0) {
            Iterator<Future<Map<String, String>>> iter = futureList.iterator();
            //noinspection WhileLoopReplaceableByForEach
            while (iter.hasNext()) {
                Future<Map<String, String>> future = iter.next();
                if (future.isDone()) {
                    imgNameList.add(future.get());
                    futureList.remove(future);
                    --index;
                }
            }
        }
        return imgNameList;
    }

    /**
     * 将源文件转化为动态的gif字符画，实际转换操作为异步执行
     * <p>
     * GIF动态图，代码中未考虑其Width、Height 过大的问题！
     * <p>
     * 推荐字体大小为 8 Pt, 缩放倍数为 4
     *
     * @param stream  源文件的文件流
     * @param outPath 输出路径
     * @return 生成的文件信息List
     */
    public Map<String, String> transfer2TextGif(InputStream stream, String outPath) throws IOException, ExecutionException, InterruptedException {
        final int fontSize = 8;
        final int zoom = 4;
        GifDecoder.GifImage gif = GifDecoder.read(stream);
        Map<Integer, BufferedImage> imgMap = new HashMap<>(16);
        for (int i = 0, frameNum = gif.getFrameCount(); i < frameNum; i++) {
            imgMap.put(i, gif.getFrame(i));
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Map<Integer, BufferedImage>> task = forkJoinPool.submit(new Text2GifForkJoinTask(imgMap, fontSize, zoom));
        while (true) {
            if (task.isDone()) {
                break;
            } else {
                TimeUnit.MILLISECONDS.sleep(10);
            }
        }
        Map<Integer, BufferedImage> resultMap = task.get();
        if (resultMap == null || resultMap.size() <= 0) {
            log.warn("图片[{}]转换失败！", outPath);
            return null;
        }
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        File outFile = this.getOutputFile(outPath, fontSize, zoom);
        boolean start = encoder.start(outFile);
        // 获取第二帧的延迟
        encoder.setDelay(gif.getDelay(1));
        resultMap.forEach((k, v) -> encoder.addFrame(v));
        start = encoder.finish();
        return this.getInfoMap(outFile.getName(), fontSize, zoom);
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
     * 自己实现的等比压缩方法，已过时，不建议使用；
     * 建议使用Thumbnails库
     *
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
     * 图片转字符再保存为图片，并写入本地磁盘
     *
     * @param chars    原图转出的二维字符数组
     * @param fontSize 转换出的图片中的文字大小，推荐 8；
     * @param zoom     缩放倍数，推荐传 8 / 2 =4；
     * @return 生成的文件的信息Map
     */
    public BufferedImage textToBufferedImage(String[][] chars, int fontSize, int zoom) {
        int width = chars[0].length * zoom;
        int height = chars.length * zoom;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图像上下文
        Graphics graphics = this.createGraphics(bufferedImage, width, height, fontSize);
        // 字体大小 pt = 像素 px 乘 3/4
        int intervalOfWidth = chars[0].length * fontSize * 3 / 4 / chars[0].length / zoom + 1;
        int intervalOfHeight = chars.length * fontSize * 3 / 4 / chars.length / zoom + 1;
        for (int i = 0, lenOfH = chars.length; i < lenOfH; i += intervalOfHeight) {
            for (int j = 0, lenOfW = chars[i].length; j < lenOfW; j += intervalOfWidth) {
                graphics.drawString(chars[i][j], j * zoom, i * zoom);
            }
        }
        graphics.dispose();
        return bufferedImage;
    }

    /**
     * 图片转字符再保存为图片，并写入本地磁盘
     *
     * @param chars    原图转出的二维字符数组
     * @param outPath  输出路径
     * @param fontSize 转换出的图片中的文字大小，推荐 8；
     * @param zoom     缩放倍数，推荐传 8 / 2 =4；
     * @return 生成的文件的信息Map
     */
    public Map<String, String> textToImage(String[][] chars, String outPath, int fontSize, int zoom) throws IOException {
        BufferedImage img = this.textToBufferedImage(chars, fontSize, zoom);
        File outImg = this.getOutputFile(outPath, fontSize, zoom);
        boolean result = ImageIO.write(img, outPath.substring(outPath.lastIndexOf(".") + 1), outImg);
        String imgName = outImg.getName();
        log.info("--------> 文本转图片{}！   img = {}", result ? "成功" : "失败", imgName);
        return this.getInfoMap(imgName, fontSize, zoom);
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

    /**
     * 图片转字符画多线程任务
     */
    private static class Text2ImgTask implements Callable<Map<String, String>> {
        private String[][] chars;
        private String outPath;
        private int fontSize;
        private int zoom;

        private static Img2TxtToyService toy = SpringBeanUtils.getBean(Img2TxtToyService.class);

        Text2ImgTask(String[][] chars, String outPath, int fontSize, int zoom) {
            this.chars = chars;
            this.outPath = outPath;
            this.fontSize = fontSize;
            this.zoom = zoom;
        }

        @Override
        public Map<String, String> call() throws Exception {
            return toy.textToImage(chars, outPath, fontSize, zoom);
        }
    }

    /**
     * GIF图片转GIF字符画多线程ForkJoin任务
     */
    private static class Text2GifForkJoinTask extends RecursiveTask<Map<Integer, BufferedImage>> {
        private Map<Integer, BufferedImage> frameMap;
        private Integer fontSize;
        private Integer zoom;

        private static Img2TxtToyService toy = SpringBeanUtils.getBean(Img2TxtToyService.class);

        Text2GifForkJoinTask(Map<Integer, BufferedImage> frameMap, Integer fontSize, Integer zoom) {
            this.frameMap = frameMap;
            this.fontSize = fontSize;
            this.zoom = zoom;
        }

        @Override
        protected Map<Integer, BufferedImage> compute() {
            final int everyThreadTaskNum = 5;
            boolean canCompute = frameMap.size() <= everyThreadTaskNum;
            Map<Integer, BufferedImage> resultMap = Collections.synchronizedMap(new TreeMap<>());
            if (canCompute) {
                frameMap.forEach((index, img) -> resultMap.put(index, toy.textToBufferedImage(toy.transfer2CharArray(img), fontSize, zoom)));
                return resultMap;
            } else {
                List<Text2GifForkJoinTask> taskList = new ArrayList<>();
                // 拆分任务的数量 24 / 5 = 4 ······ 1  ，  25 / 5 = 5
                int splitNum = frameMap.size() % everyThreadTaskNum == 0 ? (frameMap.size() / everyThreadTaskNum) : (frameMap.size() / everyThreadTaskNum + 1);
                for (int i = 1; i <= splitNum; i++) {
                    Map<Integer, BufferedImage> pre5EntryMap = frameMap.entrySet().stream().limit(5).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                    Text2GifForkJoinTask task = new Text2GifForkJoinTask(pre5EntryMap, fontSize, zoom);
                    taskList.add(task);
                    pre5EntryMap.forEach((k, v) -> frameMap.remove(k));
                    task.fork();
                }
                taskList.forEach(task -> resultMap.putAll(task.join()));
            }
            return resultMap;
        }
    }

    /**
     * 剥离的公共方法，获得返回信息Map
     *
     * @param imgName  图片名称
     * @param fontSize 字体大小
     * @param zoom     缩放倍数
     * @return 包含信息的Map
     */
    private Map<String, String> getInfoMap(String imgName, Integer fontSize, Integer zoom) {
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("font_size", String.valueOf(fontSize));
        resultMap.put("zoom", String.valueOf(zoom));
        resultMap.put("img_name", imgName);
        return resultMap;
    }

    /**
     * 提取的公共方法。
     * <p>
     * 根据相关参数，生成文件在源文件的基础上，在文件名中加上 字体大小、缩放倍数等参数配置信息
     *
     * @param outPath  源文件路径
     * @param fontSize 字体大小
     * @param zoom     缩放倍数
     * @return 输出文件的File对象
     */
    private File getOutputFile(String outPath, int fontSize, int zoom) {
        StringBuilder config = new StringBuilder("_fontsize=").append(fontSize).append("_zoom=").append(zoom);
        return new File(new StringBuilder(outPath).insert(outPath.lastIndexOf("."), config).toString());
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
    private Graphics createGraphics(BufferedImage image, int width, int height, int fontSize) {
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
