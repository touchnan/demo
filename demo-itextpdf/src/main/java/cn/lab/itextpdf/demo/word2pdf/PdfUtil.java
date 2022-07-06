package cn.lab.itextpdf.demo.word2pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/6.
 */
public class PdfUtil {
    /**
     * @param inputStream 电子合同pdf文件流
     * @param targetPath  保存路径
     * @throws Exception 异常参数
     * @Title: itextPDFAddPicture
     * @Description: 为pdf加图片(电子合同盖公章)
     */
    public static void itextPDFAddPicture(InputStream inputStream, String targetPath) throws Exception {
        // 1.1 读取模板文件
        PdfReader reader = new PdfReader(inputStream);
        // 1.2 创建文件输出流
        FileOutputStream out = new FileOutputStream(targetPath);
        // 2、创建PdfStamper对象
        PdfStamper stamper = new PdfStamper(reader, out);

        // 4、读取公章
        String path = System.getProperty("user.dir") + "/website/cont/officialSeal/officialSeal.png";
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(path));// 整个公章图片流
        BufferedImage[] imgs = ImageUtil.splitImage(bufferedImage, 1, 2);
        BufferedImage leftBufferedImage = imgs[0];// 左边公章图片流
        BufferedImage rightBufferedImage = imgs[1];// 右边公章图片流

        // 5、读公章图片
        Image image = Image.getInstance(ImageUtil.imageToBytes(bufferedImage));
        Image leftImage = Image.getInstance(ImageUtil.imageToBytes(leftBufferedImage));
        Image rightImage = Image.getInstance(ImageUtil.imageToBytes(rightBufferedImage));
        int chunkWidth = 250;// 公章大小，x轴
        int chunkHeight = 250;// 公章大小，y轴
        // 获取pdf页面的高和宽
        Rectangle pageSize = reader.getPageSize(1);
        float height = pageSize.getHeight();
        float width = pageSize.getWidth();
        // 6、为pdf每页加印章
        // 设置公章的位置
        float xL = width - chunkWidth / 2 - 2;
        float yL = height / 2 - chunkHeight / 2 - 280;

        float xR = width - chunkHeight / 2 + chunkHeight / 8 + 4;
        float yR = yL;
        // 6.1 第一页盖左章
        leftImage.scaleToFit(chunkWidth, chunkHeight);// 公章大小
        leftImage.setAbsolutePosition(xL, yL);// 公章位置
        // 6.2 第二页盖右章
        rightImage.scaleToFit(chunkWidth, chunkHeight);// 公章大小
        rightImage.setAbsolutePosition(xR, yR);// 公章位置
        int pdfPages = reader.getNumberOfPages();// pdf页面页码
        // 遍历为每页盖左章或右章
        for (int i = 1; i <= pdfPages; i++) {
            if (i % 2 == 0) {// 盖右章
                stamper.getOverContent(i).addImage(rightImage);
            } else {// 盖左章
                stamper.getOverContent(i).addImage(leftImage);
            }
        }

        // 6.3 最后一页盖公章
        image.scaleToFit(chunkWidth, chunkHeight);
        //新建一个PDF解析对象
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        Rectangle2D.Float position = getPosition(stamper, reader, "盖章处");
        //  image.setAbsolutePosition(width/2 + 32, height-chunkHeight -350);
        //得到的位置有些许偏差，自行调节
        image.setAbsolutePosition(position.x + 150, position.y - 110);
        stamper.getOverContent(pdfPages).addImage(image);

        // 7、关闭相关流
        stamper.close();
        out.close();
        reader.close();
        inputStream.close();
    }


    /**
     * @Author: wuyongqiang
     * @Description:获取pdf中关键字位置，得到的位置有些许偏差，根据实际显示结果自行调节
     * @Date: 2021/12/7  9:53
     * @Param stamper:
     * @Param reader:
     * @Param str:
     * @return: com.itextpdf.awt.geom.Rectangle2D.Float
     **/
    public static Rectangle2D.Float getPosition(PdfStamper stamper, PdfReader reader, String str) throws IOException {
        //新建一个PDF解析对象
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        Rectangle2D.Float position = null;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte pdfContentByte = stamper.getOverContent(i);
            //新建一个ImageRenderListener对象，该对象实现了RenderListener接口，作为处理PDF的主要类
            TestRenderListener listener = new TestRenderListener();
            //解析PDF，并处理里面的文字
            parser.processContent(i, listener);
            //获取文字的矩形边框
            List<Rectangle2D.Float> rectText = listener.rectText;
            List<String> textList = listener.textList;
            List<Float> listY = listener.listY;
            List<Map<String, Rectangle2D.Float>> list_text = listener.rows_text_rect;
            for (String strtext : textList) {
                if (strtext.contains(str)) {
                    int index = textList.indexOf(strtext);
                    position = rectText.get(index);
                }
            }
        }
        return position;
    }
}

