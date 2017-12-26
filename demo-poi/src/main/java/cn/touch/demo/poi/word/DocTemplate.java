/**
 * 
 */
package cn.touch.demo.poi.word;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 22, 2017.
 */
public class DocTemplate {
	public static void main(String[] args) {
	       File templateFile = new File("/template/template1.doc");


	        ByteArrayOutputStream ostream = null;
	        try {
	            FileInputStream in = new FileInputStream(templateFile);
	            HWPFDocument hwpfDocument = new HWPFDocument(in);
	            // 替换读取到的 word 模板内容的指定字段
	            Map<String,String> params = new HashMap<>();
	            params.put("$PZJG$","pzjg");
	            params.put("$GDNR$","gdnr");
	            params.put("$JSYY$","JSYY");
	            params.put("$CHYF$","CHYF");
	            params.put("$XZGF$","XZGF");
	            Range range = hwpfDocument.getRange();
	            for(Map.Entry<String,String> entry:params.entrySet()){
	                range.replaceText(entry.getKey(),entry.getValue());
	            }
	            
	            
//	            // 输出 word 内容文件流，提供下载
//	            response.reset();
//	            response.setContentType("application/x-msdownload");
//
//	            // 随机生成一个文件名
//	            UUID randomUUID = UUID.randomUUID();
//	            String attachmentName = randomUUID.toString();
//	            response.addHeader("Content-Disposition", "attachment; filename=\""+ attachmentName + ".doc\"");
//	            ostream = new ByteArrayOutputStream();
//	            ServletOutputStream servletOS = response.getOutputStream();
	            hwpfDocument.write(ostream);
//	            servletOS.write(ostream.toByteArray());
//	            servletOS.flush();
//	            servletOS.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	}

}
