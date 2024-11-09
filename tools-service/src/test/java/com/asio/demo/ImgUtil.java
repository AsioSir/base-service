package com.asio.demo;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import sun.misc.BASE64Encoder;
public class ImgUtil {

    static String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'.";
    static String base = "love周五放假";//使用这个字符

    // 像素点取值间隔
    private static final int split = 2;
    // 像素点步长
    private static final int speed = 10;
    // 放大倍数
    private static final int multiple = 6;

    public static void main(String[] args) throws Exception {
        String inputImgPath = "E:/img/img1.jpg";
        String outImgPath = "E:/img/img2.jpg";
        txtToImage(inputImgPath, outImgPath);
//
//        String outTxtPath = "E:/img/img2.txt";
//        imageToTxt(inputImgPath, outTxtPath);

//            String path = "E:/img/img1.jpg";//导入的图片
//            String base = "love";//将会用这个字符串里的字符填充图片
//            BufferedImage image = ImageIO.read(new File(path));//读入图片，并用图片缓冲区对象来接收
//
//            //双层for循环，遍历图片
//            for (int y = 0; y < image.getHeight(); y++) {//先竖向遍历，再横向遍历，即一行一行的找，后面也会一行一行的打印
//                for (int x = 0; x < image.getWidth(); x++) {
//                    int color = image.getRGB(x, y);//图片缓冲区自带的方法，可以得到当前点的颜色值，返回值是int类型
//                    int r=(color>>16)&0xff;
//                    int g=(color>>8)&0xff;
//                    int b=color&0xff;
//                    float gray = 0.299f * r + 0.578f * g + 0.114f * b;//灰度值计算公式，固定比例，无需理解
//                    int index = Math.round(gray * (base.length()) / 255);
//                    if(index>=base.length()) {
//                        System.out.print(" ");//白色的地方打空格，相当于白色背景，这样图片轮廓比较明显
//                    }else {
//                        System.out.print(base.charAt(index));//有颜色的地方打字符
//                    }
//                }
//                System.out.println();//一行打完，换行
//
//        }

    }


    public static void imageToTxt(String inputImgPath, String txtPath) throws Exception {
        BufferedImage bi = ImageIO.read(new File(inputImgPath));
        try {
            int width = bi.getWidth();
            int height = bi.getHeight();
            boolean flag = false;
            String result = "";
            for (int i = 0; i < height; i += 2) {
                for (int j = 0; j < width; j++) {
                    int pixel = bi.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
                    int red = (pixel & 0xff0000) >> 16;
                    int green = (pixel & 0xff00) >> 8;
                    int blue = (pixel & 0xff);
                    float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                    int index = Math.round(gray * (base.length() + 1) / 255);
                    result += index >= base.length() ? " " : String.valueOf(base.charAt(index));
                }
                result += "\r\n";
            }
            flag = writeTxtFile(result,txtPath);//保存字符到文本文件
            System.out.println(flag?"图片转字符保存成功":"图片转字符保存失败");
        } catch (Exception e) {
            System.out.println("图片转字符异常"+e.getMessage());
        }
    }


    /**
     * 图片转字符再保存为图片
     * @param inputImgPath 原图地址
     * @param outImgPath 目标地址
     * @return String
     */
    public static void txtToImage(String inputImgPath, String outImgPath) throws Exception {
        System.out.println("进来的时间"+System.currentTimeMillis());
        BufferedImage bi = ImageIO.read(new File(inputImgPath));

        File imageFile = new File(outImgPath);
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        System.out.println(width*multiple + " " + height*multiple);
        BufferedImage bufferedImage = new BufferedImage(width*multiple, height*multiple, BufferedImage.TYPE_INT_RGB);
        // 获取图像上下文
        Graphics g = createGraphics(bufferedImage, width*multiple, height*multiple, speed);
        // 图片中文本行高
        final int Y_LINEHEIGHT = speed;
        int lineNum = 1;
        for (int i = miny; i < height; i += split) {
            for (int j = minx; j < width; j += split) {
                int pixel = bi.getRGB(j, i); // 下面三行代码将一个数字转换为RGB数字
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                int index = Math.round(gray * (base.length() + 1) / 255);
//				char c = ascii.charAt((int) (gray / 255 * ascii.length()));
//				char c = toChar((int) gray);
//				char c = toChar(index);
                String c = index >= base.length() ? " " : String.valueOf(base.charAt(index));
                g.drawString(String.valueOf(c), j*multiple, i*multiple);
            }
            lineNum++;
        }
        g.dispose();
        // 保存为jpg图片
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imageFile);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            OutputStream out = encoder.getOutputStream();
            BASE64Encoder base64Encoder = new BASE64Encoder();
            encoder.encode(bufferedImage);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 画板默认一些参数设置
     * @param image 图片
     * @param width 图片宽
     * @param height 图片高
     * @param size 字体大小
     * @return
     */
    private static Graphics createGraphics(BufferedImage image, int width,
                                           int height, int size) {
        Graphics g = image.createGraphics();
        g.setColor(null); // 设置背景色
        g.fillRect(0, 0, width, height);// 绘制背景
        g.setColor(Color.BLACK); // 设置前景色
        g.setFont(new Font("微软雅黑", Font.PLAIN, size)); // 设置字体
        return g;
    }



    /**
     * 字符保存到txt文件中
     * @param imageStr 字符
     * @param txtPath  txt文件
     * @return boolean
     * @throws Exception
     */
    private static boolean writeTxtFile(String imageStr, String txtPath) throws Exception{
        // 先读取原有文件内容，然后进行写入操作
        boolean flag = false;
        String filein = imageStr;
        String temp = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            // 文件路径
            File file = new File(txtPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            // 保存该文件原有的内容
            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
            }
            buf.append(filein);
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (IOException e) {
            System.out.println("文件保存失败"+e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }

}
