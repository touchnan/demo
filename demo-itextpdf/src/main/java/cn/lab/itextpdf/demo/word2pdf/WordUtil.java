package cn.lab.itextpdf.demo.word2pdf;

import fr.opensagres.xdocreport.utils.StringUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2022/7/6.
 */
public class WordUtil {
    /**
     * @Author: wuyongqiang
     * @Description:替换word中需要替换的特殊字符
     * @Date: 2021/12/7  16:59
     * @Param srcPath: 需要替换的文档全路径
     * @Param exportFile: 替换后文档的保存路径
     * @Param contentMap: {key:将要被替换的内容,value:替换后的内容}
     * @Param replaceTableMap: {key:将要被替换的表格内容,value:替换后的表格内容}
     * @return: boolean 返回成功状态
     **/
    public static boolean replaceAndGenerateWord(String  srcPath, String exportFile, Map<String, String> contentMap, Map<String, String> replaceTableMap) throws IOException {
        boolean bool = true;
        FileInputStream inputStream = new FileInputStream(srcPath);
        XWPFDocument document = new XWPFDocument(inputStream);
        // 替换段落中的指定文字
        Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
        while (itPara.hasNext()) {
            XWPFParagraph paragraph = itPara.next();
            commonCode(paragraph, contentMap);
        }
        // 替换表格中的指定文字
        Iterator<XWPFTable> itTable = document.getTablesIterator();
        while (itTable.hasNext()) {
            XWPFTable table = itTable.next();
            int rcount = table.getNumberOfRows();
            for (int i = 0; i < rcount; i++) {
                XWPFTableRow row = table.getRow(i);
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    //单元格中有段落，得做段落处理
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        commonCode(paragraph, replaceTableMap);
                    }
                }
            }
        }

        FileOutputStream outStream = new FileOutputStream(exportFile);
        document.write(outStream);
        outStream.close();
        inputStream.close();


        return bool;
    }


    /**
     * @Author: wuyongqiang
     * @Description:替换内容
     * @Date: 2021/12/7  16:56
     * @Param paragraph: 被替换的文本信息
     * @Param contentMap: {key:将要被替换的内容,value:替换后的内容}
     * @return: void
     **/
    private static void commonCode(XWPFParagraph paragraph,Map<String, String> contentMap){
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            String oneparaString = run.getText(run.getTextPosition());
            if (StringUtils.isEmpty(oneparaString)){
                continue;
            }
            for (Map.Entry<String, String> entry : contentMap.entrySet()) {
                oneparaString = oneparaString.replace(entry.getKey(), StringUtils.isEmpty(entry.getValue()) ? "--" : entry.getValue());
            }
            run.setText(oneparaString, 0);
        }
    }



    /**
     * @Author: wuyongqiang
     * @Description:验证license许可凭证
     * @Date: 2021/12/7  16:57
     * @return: boolean 返回验证License状态
     **/
    private static boolean getLicense() {
        boolean result = true;
        try {
            String path =  System.getProperty("user.dir")+"/website/cont/license.xml";
            //FIXME
//            new License().setLicense(new FileInputStream(new File(path).getAbsolutePath()));
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @Author: wuyongqiang
     * @Description:word转pdf
     * @Date: 2021/12/7  16:58
     * @Param wordPath: word 全路径，包括文件全称
     * @Param pdfPath: pdf 保存路径，包括文件全称
     * @return: boolean 返回转换状态
     **/
    public static boolean wordConverterToPdf(String wordPath, String pdfPath)  {

        boolean bool = false;
        // 验证License,若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) return bool;
        try {
            FileOutputStream os = new FileOutputStream(new File(pdfPath));// 新建一个pdf文档输出流
            //FIXME
//            com.aspose.words.Document doc = new com.aspose.words.Document(wordPath);// Address是将要被转化的word文档
//            doc.save(os, com.aspose.words.SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            os.close();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("word转PDF转换异常！！");
        }

        return bool;
    }



    /**
     * @Author: wuyongqiang
     * @Description: 在word表格中指定位置插入一行，并将某一行的样式复制到新增行
     * @Date: 2021/12/7  16:58
     * @Param table: 需要插入的表格
     * @Param copyrowIndex: 需要复制的行位置
     * @Param newrowIndex: 需要新增一行的位置
     * @return: void 返回类型
     **/
    public static void insertRow(XWPFTable table, int copyrowIndex, int newrowIndex) {
        // 在表格中指定的位置新增一行
        XWPFTableRow targetRow = table.insertNewTableRow(newrowIndex);
        // 获取需要复制行对象
        XWPFTableRow copyRow = table.getRow(copyrowIndex);
        //复制行对象
        targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());
        //获取需要复制的行的列
        List<XWPFTableCell> copyCells = copyRow.getTableCells();
        //复制列对象
        XWPFTableCell targetCell = null;
        for (int i = 0; i < copyCells.size(); i++) {
            XWPFTableCell copyCell = copyCells.get(i);
            targetCell = targetRow.addNewTableCell();
            if (copyCell.getParagraphs() != null && copyCell.getParagraphs().size() > 0) {
                targetCell.getParagraphs().get(0).getCTP().setPPr(copyCell.getParagraphs().get(0).getCTP().getPPr());
                if (copyCell.getParagraphs().get(0).getRuns() != null
                        && copyCell.getParagraphs().get(0).getRuns().size() > 0) {
                    XWPFRun cellR = targetCell.getParagraphs().get(0).createRun();
                    cellR.setBold(copyCell.getParagraphs().get(0).getRuns().get(0).isBold());
                }
            }
        }

    }
}
