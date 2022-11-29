package cn.touch.demo.vertx.demo_vertx;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/10/19.
 */
public class TestTika {

    /**
     * 根据Tika得到文档的内容，这种比下面那种获取的要简单很多，
     * 据tika的文档上说，效率没有下面的那种高，可能封装的比较多
     * @param f
     * @return
     * @throws IOException
     * @throws TikaException
     */
    public static String tikaTool(File f) throws IOException, TikaException {
        Tika tika = new Tika();
        Metadata metadata = new Metadata();
//        metadata.set(Metadata.AUTHOR, "空号");//重新设置文档的媒体内容
//        metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
        String str = tika.parseToString(new FileInputStream(f),metadata);
        for(String name:metadata.names()) {
            System.out.println(name+":"+metadata.get(name));
        }
        return str;
    }

    /**
     * 根据Parser得到文档的内容
     *
     * @param f
     * @return
     */
    public static String fileToTxt(File f) {
        Parser parser = new AutoDetectParser();//自动检测文档类型，自动创建相应的解析器
        InputStream is = null;
        try {
            Metadata metadata = new Metadata();
//            metadata.set(Metadata.AUTHOR, "空号");//重新设置文档的媒体内容
//            metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
            is = new FileInputStream(f);
            ContentHandler handler = new BodyContentHandler();
            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);


            parser.parse(is, handler, metadata, context);
            for (String name : metadata.names()) {
                System.out.println(name + ":" + metadata.get(name));
            }
            return handler.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static String path = "src/test/java/cn/touch/demo/vertx/demo_vertx/jdiff/";
    public static String s_file = path + "test1.txt";
    public static String d_file = path + "test2.txt";
    public static String m_file = path + "test3.txt";
    public static void main(String[] args) throws TikaException, IOException {
        String root = "C:\\Users\\touch\\Downloads";
//        String fileName = root+"\\"+"中小企业声明函（正确）.pdf";
//        String fileName1 = root+"\\"+"中小企业声明函（错误）.pdf";
        String fileName2 = root+"\\"+"政府采购制度文件汇编2022版.pdf";


//        System.out.println("1"+TestTika.fileToTxt(new File(fileName2)));
        System.out.println("1"+TestTika.tikaTool(new File(fileName2)));
    }
}
