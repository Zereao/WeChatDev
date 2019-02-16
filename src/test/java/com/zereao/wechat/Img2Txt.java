package com.zereao.wechat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Darion Mograine H
 * @version 2019/02/15  11:15
 */
public class Img2Txt {
    /**
     * 定义把灰度值转化成的txt字符集
     */
    private static String toChar = "@#&$%*o!;.";
//    private static String toChar = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/|()1{}[]?-_+~<>i!lI;:,\"^`'. ";


    /**
     * 函数主体
     */

    public static void main(String[] args) throws IOException {
//        /*  需要转换的图片的文件地址  */
//        String inputFileName = "C:\\Users\\Jupiter\\Desktop\\imagetool\\src\\main\\resources\\20180619202355878.jpg";
////        String inputFileName = "C:\\Users\\Jupiter\\Desktop/微信图片_20190215122021.jpg";
//        BufferedImage image = ImageIO.read(new File(inputFileName));
//        /**/
//        //绘图方法实现绘图
//        BufferedImage scaled = getScaledImg(image);
////        String output = "C:\\Users\\Jupiter\\Desktop/图片输出.jpg";
////        txtToImage(scaled, output);
//        //图片转成txt字符集的方法
//        char[][] array = getImageMatrix(scaled);
//        //输出字符
//        String output = "C:\\Users\\Jupiter\\Desktop/图片输出.txt";
//        File outputFile = new File(output);
//        if (outputFile.isFile() && outputFile.exists()) {
//            boolean result = outputFile.delete();
//        }
//        FileWriter writer = new FileWriter(outputFile);
//        for (char[] cs : array) {
//            for (char c : cs) {
//                writer.append(c);
//            }
//            writer.append("\n");
//            writer.flush();
//        }
//        System.out.println("输出完毕");
    }

    /**
     * 图片转字符再保存为图片
     *
     * @param img        原图
     * @param outPutPath 输出路径
     * @return BufferedImage
     */
    public static BufferedImage txtToImage(BufferedImage img, String outPutPath) throws IOException {
        File imageFile = new File(outPutPath);
        if (!imageFile.exists()) {
            boolean newFile = imageFile.createNewFile();
        }
        int width = img.getWidth();
        int height = img.getHeight();
        int minX = img.getMinX();
        int minY = img.getMinY();
        int fontSize = 7;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图像上下文
        Graphics graphics = createGraphics(bufferedImage, width, height, fontSize);
        // 图片中文本行高
        for (int i = minY; i < width; i++) {
            for (int j = minX; j < height; j++) {
                int pixel = img.getRGB(i, j);
                // 下面三行代码将一个数字转换为RGB数字
//                int red = (pixel & 0xff0000) >> 16;
//                int green = (pixel & 0xff00) >> 8;
//                int blue = (pixel & 0xff);
                int red = Integer.valueOf(Integer.toBinaryString(pixel).substring(0, 8), 2);
                int green = (pixel & 0xff00) >> 8;
                int blue = pixel & 0xff;
                int gray = (int) (0.299f * red + 0.587f * green + 0.114f * blue);
                int index = gray / (256 / toChar.length() + 1);
                String c = String.valueOf(toChar.charAt(index));
                graphics.drawString(c, j, i);
            }
        }
        graphics.dispose();
        // 保存为jpg图片
        try {
            ImageIO.write(bufferedImage, "jpg", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;

    }

    /**
     * 画板默认一些参数设置
     *
     * @param image    图片
     * @param width    图片宽
     * @param height   图片高
     * @param fontSize 字体大小
     * @return
     */
    private static Graphics createGraphics(BufferedImage image, int width, int height, int fontSize) {
        Graphics g = image.createGraphics();
        // 设置背景色
        g.setColor(null);
        // 绘制背景
        g.fillRect(0, 0, width, height);
        // 设置前景色
        g.setColor(Color.BLACK);
        // 设置字体
        g.setFont(new Font("宋体", Font.PLAIN, fontSize));
        return g;
    }
}
