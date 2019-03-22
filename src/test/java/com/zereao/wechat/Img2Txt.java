package com.zereao.wechat;

import com.zereao.wechat.common.utils.ThreadPoolUtils;
import com.zereao.wechat.service.command.toys.Img2TxtToyService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Darion Mograine H
 * @version 2019/02/15  11:15
 */
public class Img2Txt {

    @Test
    void test008() throws IOException {
        String path = "/Users/jupiter/Downloads/WX20190223-232232@2x.png";
        File file = new File(path);
        BufferedImage img = Thumbnails.of(file).size(1000, 1000).asBufferedImage();
        String out = "/Users/jupiter/Downloads/输出.png";
        boolean result = ImageIO.write(img, "png", new File(out));
        System.out.println(result);
    }

    @Test
    void test007() throws IOException, InterruptedException, ExecutionException {
//        Img2TxtToyService toy = new Img2TxtToyService();
//        String path = "/Users/jupiter/Downloads/WX20190223-232232@2x.png";
//        toy.transfer2TextImg(new FileInputStream(path), path);

    }

    @Test
    void test006() throws InterruptedException {
        InnerTask task = new InnerTask(30);
        new ForkJoinPool().submit(task);
        while (true) {
            if (task.isDone()) {
                System.out.println("=======================已完成");
                break;
            } else {
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println("====== 等待 =====");
            }
        }
        System.out.println("=++++++++++++++++++++++");
    }

    private class InnerTask extends RecursiveTask<String> {
        private Integer i;

        public InnerTask(Integer i) {
            this.i = i;
        }

        @Override
        protected String compute() {
            if (i <= 5) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("执行最小单元");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "测试完毕";
            } else {
                int num = i / 5 + 1;
                List<InnerTask> taskList = new ArrayList<>();
                for (int j = 0; j < num; j++) {
                    InnerTask task = new InnerTask(5);
                    taskList.add(task);
                    task.fork();
                }
                for (InnerTask task : taskList) {
                    System.out.println("join 前");
//                    task.join();
                    System.out.println("Join侯曼");
                    System.out.println();
                }
            }


            return null;
        }
    }

    @Test
    void test005() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("第一线程");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("第二线程");
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        thread.join();
        System.out.println("完毕");
        thread1.start();
    }

    @Test
    void test004() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        for (Integer integer : list) {
            if (integer.equals(5)) {
                list.remove(5);
            }
        }

    }

    @Test
    void test003() {
        String path1 = "/Users/jupiter/Documents/test/";
//        FileUtils.
        File file = new File(path1);
        System.out.println(file.isDirectory());
        System.out.println(file.exists());
    }

    @Test
    void test002() throws IOException {
        String gifPath = "C:/Users/Jupiter/Desktop/QQ图片20190215160757.gif";
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(new FileImageInputStream(new File(gifPath)));
        int num = reader.getNumImages(true);
        BufferedImage[] frames = new BufferedImage[num];
        for (int i = 0; i < num; i++) {
            BufferedImage imgFrame = reader.read(i);

        }

    }


    @Test
    void test001() throws IOException {
//        Img2TxtToyService toy = new Img2TxtToyService();
        String file = "C:/Users/Jupiter/Desktop/微信图片_20190215122021.jpg";
        BufferedImage img = ImageIO.read(new File(file));


        String compressedPath = "C:/Users/Jupiter/Desktop/20180619202355878-压缩.jpg";

        int width = img.getWidth(), height = img.getHeight();
        boolean maxOver1000 = (width > height ? width : height) > 1000;
        if (maxOver1000) {
            ImageIO.write(img, "jpg", new File(compressedPath));
        }

//        BufferedImage c = toy.compress(img, 100);

//        ImageIO.write(c, "jpg", new File(compressedPath));
        String outPath = "C:/Users/Jupiter/Desktop/2weixin0180619202355878.jpg";

//        String[][] chars = toy.transfer2CharArray(img);
    }

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
