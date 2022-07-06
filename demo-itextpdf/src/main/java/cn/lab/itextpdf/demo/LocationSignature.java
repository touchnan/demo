package cn.lab.itextpdf.demo;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/6.
 *
 * java为PDF盖(签)电子签章--位置定位
 * https://blog.csdn.net/qq_41644069/article/details/121344421
 */
public class LocationSignature {
    public void sign(InputStream p12Stream, //p12 路径
                     char[] password,
                     InputStream src,//需要签章的pdf文件路径
                     OutputStream dest,// 签完章的pdf文件路径
                     String reason,//签名的原因，显示在pdf签名属性中，随便填
                     String location,//签名的地点，显示在pdf签名属性中，随便填
                     String chapterPath//电子签章的图片
    ) throws GeneralSecurityException, IOException, DocumentException {
        //读取keystone，获得私钥和证书链
        KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
        pkcs12.load(p12Stream, password);
        String alias = pkcs12.aliases().nextElement();
        PrivateKey key = (PrivateKey) pkcs12.getKey(alias, password);
        Certificate[] chain = pkcs12.getCertificateChain(alias);

        //下边的步骤都是固定的，照着写就行了，没啥要解释的
        // Creating the reader and the stamper，开始pdfreader
        PdfReader reader = new PdfReader(src);

        //目标文件输出流
        //创建签章工具PdfStamper ，最后一个boolean参数
        //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
        //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
        PdfStamper stamper = PdfStamper.createSignature(reader, dest, '\0', null, false);
        // 获取数字签章属性对象，设定数字签章的属性
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);

        //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名域名称不能一样
        //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
        //四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
        appearance.setVisibleSignature(new Rectangle(0, 800, 100, 700), 1, "sig1");

        //读取图章图片，这个image是itext包的image
        Image image = Image.getInstance(chapterPath);
        appearance.setSignatureGraphic(image);
        appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
        //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

        // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
        // 摘要算法
        BouncyCastleDigest digest = new BouncyCastleDigest();
        // 签名算法
        PrivateKeySignature signature = new PrivateKeySignature(key, DigestAlgorithms.SHA1, null);
        // 调用itext签名方法完成pdf签章CryptoStandard.CMS 签名方式，建议采用这种
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
    }

    public static void main(String[] args) throws IOException, DocumentException, GeneralSecurityException {
        LocationSignature locationSignature = new LocationSignature();
        String KEYSTORE = "d:\\test\\itextpdf\\client.p12";
        char[] PASSWORD = "kx20190927".toCharArray();//keystory密码
        String SRC = "d:\\test\\itextpdf\\javase.pdf";//原始pdf
        String DEST = "d:\\test\\itextpdf\\signjavase.pdf";//签名完成的pdf
        String chapterPath = "d:\\test\\itextpdf\\stamp.png";//签章图片
        String reason = "数据不可更改";
        String location = "hangzhou";

        locationSignature.sign(new FileInputStream(KEYSTORE), PASSWORD, new FileInputStream(SRC), new FileOutputStream(DEST), reason, location, chapterPath);
        System.out.println("签章完成");
    }
}
