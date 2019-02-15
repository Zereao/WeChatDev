package com.zereao.wechat.service.command.toys;

import com.sun.image.codec.jpeg.ImageFormatException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;

/**
 * 图片转txt
 *
 * @author Darion Mograine H
 * @version 2019/02/15  15:00
 */
@Slf4j
@Service
@SuppressWarnings("Duplicates")
public class Img2TxtToyService {
    //    @Value("${toys.img2txt.elements}")
    private static String strElements = "@#&$%*o!;.";

    /**
     * 图片转字符串，宽 x 高 = 1 x 2
     *
     * @param img BufferedImage图片
     * @return 转换后的字符串
     */
    public String transfer2String1x2(BufferedImage img) {
        int len = strElements.length();
        int width = img.getWidth();
        int height = img.getHeight();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < height; i += 2) {
            for (int j = 0; j < width; j++) {
                // (0,0),(1,0),(2,0),(3,0),(4,0),换行,(0,1),(1,1),(2,1),(3,1),(4,1) 这个顺序开始读的
                int pixel = img.getRGB(j, i);
                // 下面三行代码将一个数字转换为RGB数字
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                int index = Math.round(gray * (len + 1) / 255);
                result.append(index >= len ? " " : String.valueOf(strElements.charAt(index)));
            }
            result.append("\r\n");
        }
        return result.toString();
    }

    /**
     * 图片转字符串，宽 x 高 = 1 x 1
     *
     * @param img BufferedImage图片
     * @return 转换后的字符串
     */
    public String transfer2String1x1(BufferedImage img) {
        int len = strElements.length();
        int width = img.getWidth();
        int height = img.getHeight();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // (0,0),(1,0),(2,0),(3,0),(4,0),换行,(0,1),(1,1),(2,1),(3,1),(4,1) 这个顺序开始读的
                int pixel = img.getRGB(j, i);
                // 下面三行代码将一个数字转换为RGB数字
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                int index = Math.round(gray * (len + 1) / 255);
                result.append(index >= len ? " " : String.valueOf(strElements.charAt(index)));
            }
            result.append("\r\n");
        }
        return result.toString();
    }

    /**
     * 图片转字符数组，宽 x 高 = 1 x 2
     * 返回的二维数组的大小等于 Height ； 元素的大小等于 Width
     *
     * @param img BufferedImage图片
     * @return 转换后二维字符数组
     */
    public String[][] transfer2CharArray1x2(BufferedImage img) {
        int len = strElements.length();
        int width = img.getWidth();
        int height = img.getHeight();
        int xSize = height % 2 == 0 ? height / 2 : height / 2 + 1;
        String[][] result = new String[xSize][width];
        for (int i = 0; i < height; i += 2) {
            for (int j = 0; j < width; j++) {
                int pixel = img.getRGB(j, i);
                // 下面三行代码将一个数字转换为RGB数字
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                int index = Math.round(gray * (len + 1) / 255);
                result[i / 2][j] = index >= len ? " " : String.valueOf(strElements.charAt(index));
            }
        }
        return result;
    }

    /**
     * @param source   需要压缩的源文件的BufferedImage对象
     * @param longSize 压缩后较长边的大小
     * @return 压缩后的BufferedImage对象
     */
    public BufferedImage compress(BufferedImage source, int longSize) {
        int type = source.getType();
        int oldWidth = source.getWidth();
        int oldHeight = source.getHeight();
        double wZoom = (double) longSize / oldWidth;
        double hZoom = (double) longSize / oldHeight;
        int width = longSize, height = longSize;
        if (wZoom < hZoom) {
            wZoom = hZoom;
            width = (int) (wZoom * source.getWidth());
        } else {
            hZoom = wZoom;
            height = (int) (hZoom * source.getHeight());
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
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(wZoom, hZoom));
        g.dispose();
        return target;
    }

    /**
     * 图片转字符数组，宽 x 高 = 1 x 1
     * 返回的二维数组的大小等于 Height ； 元素的大小等于 Width
     *
     * @param img BufferedImage图片
     * @return 转换后二维字符数组
     */
    public String[][] transfer2CharArray1x1(BufferedImage img) {
        int len = strElements.length();
        int width = img.getWidth();
        int height = img.getHeight();
        String[][] result = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = img.getRGB(j, i);
                // 下面三行代码将一个数字转换为RGB数字
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                int index = Math.round(gray * (len + 1) / 255);
                result[i][j] = index >= len ? " " : String.valueOf(strElements.charAt(index));
            }
        }
        return result;
    }

    /**
     * 图片转字符再保存为图片
     *
     * @param img     原图BufferedImage对象
     * @param outPath 输出文件夹
     * @param zoom    缩放倍数，推荐 传入 7/8
     */
    public void txtToImage(BufferedImage img, String outPath, int zoom) throws IOException {
        int extIndex = outPath.lastIndexOf(".");
        String config = "_缩放" + zoom + "倍";
        String realOutPath = new StringBuilder(outPath).insert(extIndex, config).toString();
        File outImg = new File(realOutPath);
        if (!outImg.exists()) {
            boolean newImgFile = outImg.createNewFile();
        }
        String[][] chars = this.transfer2CharArray1x1(img);
        int width = img.getWidth() * zoom;
        int height = chars.length * zoom;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图像上下文
        Graphics graphics = createGraphics(bufferedImage, width, height, zoom);
        for (int i = 0, lenOfH = chars.length; i < lenOfH; i++) {
            for (int j = 0, lenOfW = chars[i].length; j < lenOfW; j++) {
                graphics.drawString(chars[i][j], j * zoom, i * zoom);
            }
        }
        graphics.dispose();
        // 保存为jpg图片
        try {
            File outImg2 = new File("C:/Users/Jupiter/Desktop/未压缩_20190215122021.jpg");
            if (!outImg2.exists()) {
                boolean newImgFile = outImg.createNewFile();
            }


            BufferedImage commpressed = this.compress(bufferedImage, 500);
            boolean result = ImageIO.write(commpressed, outPath.substring(extIndex), outImg);
            System.out.println(result);
        } catch (ImageFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Img2TxtToyService toy = new Img2TxtToyService();
        String inputFileName = "C:/Users/Jupiter/Desktop/20180619202355878.jpg";
        BufferedImage image = ImageIO.read(new File(inputFileName));
        String outputFile = "C:/Users/Jupiter/Desktop/zhangsan压缩后的.jpg";

        ImageIO.write(toy.compress(image, 500), "jpg", new File(outputFile));

//        ImageIO.write(toy.compress(image, 500), "jpg", new File(outputFile));


//        String outputFileName = "C:/Users/Jupiter/Desktop/20180619202355878.jpg.txt";
//        String[][] chars = toy.transfer2CharArray(image);
//        toy.write2File(chars, outputFileName);
//        toy.txtToImage(image, outputFile, 8);
    }

    class task implements Runnable {
        BufferedImage image;
        String outputFile;
        int i;

        public task(BufferedImage image, String outputFile, int i) {
            this.image = image;
            this.outputFile = outputFile;
            this.i = i;
        }

        @Override
        public void run() {
            Img2TxtToyService toy = new Img2TxtToyService();
            try {
                toy.txtToImage(image, outputFile, 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write2File(String[][] content, String outPath) throws IOException {
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
        graphics.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        return graphics;
    }
}
