package cn.lab.itextpdf.demo.word2pdf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/6.
 */
public class ImageUtil {
    /**
     * @Author: wuyongqiang
     * @Description:分割图片
     * @Date: 2021/12/9  8:34
     * @Param image: 图片BufferedImage流
     * @Param rows: 分割行
     * @Param cols: 分割列
     * @return: java.awt.image.BufferedImage[]  返回分割后的图片流
     **/
    public static BufferedImage[] splitImage(BufferedImage image, int rows, int cols) {
        // 分割成4*4(16)个小图
        int chunks = rows * cols;
        // 计算每个小图的宽度和高度
        int chunkWidth = image.getWidth() / cols + 3;// 向右移动3
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage[] imgs = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //设置小图的大小和类型
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, BufferedImage.TYPE_INT_RGB);
                //写入图像内容
                Graphics2D gr = imgs[count].createGraphics();
                // 增加下面代码使得背景透明
                imgs[count] = gr.getDeviceConfiguration().createCompatibleImage(chunkWidth, chunkHeight, Transparency.TRANSLUCENT);
                gr.dispose();
                gr = imgs[count].createGraphics();
                gr.drawImage(image, 0, 0,
                        chunkWidth, chunkHeight,
                        chunkWidth * y, chunkHeight * x,
                        chunkWidth * y + chunkWidth,
                        chunkHeight * x + chunkHeight, null);
                gr.dispose();
                count++;
            }
        }
        return imgs;
    }


    /**
     * @Author: wuyongqiang
     * @Description:  将BufferedImage转换成字节数组
     * @Date: 2021/12/6  17:31
     * @Param bufferedImage:
     * @return: byte[]
     **/
    public static   byte[] imageToBytes(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bufferedImage, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return imageInByte;

    }
}
