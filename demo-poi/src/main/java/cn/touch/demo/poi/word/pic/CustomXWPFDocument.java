/**
 * 
 */
package cn.touch.demo.poi.word.pic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;  
import org.apache.poi.xwpf.usermodel.XWPFDocument;  
import org.apache.poi.xwpf.usermodel.XWPFParagraph;  
import org.apache.xmlbeans.XmlException;  
import org.apache.xmlbeans.XmlToken;  
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;  
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;  
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline; 

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Jul 25, 2017.
 */

/*-
 * http://blog.csdn.net/hujun_123456/article/details/70241777
 * 
 * http://www.it165.net/pro/html/201108/451.html
 * http://maclab.iteye.com/blog/1749049
 * 
 * http://www.cnblogs.com/dreammyle/p/5159267.html
 * 
 * http://blog.csdn.net/kimmking/article/details/8205439
 * 
 * http://blog.csdn.net/kimmking/article/details/8203372
 * 
 * http://blog.csdn.net/willcold/article/details/39969565
 * 
 * http://blog.csdn.net/pengchong333/article/details/53816356
 * 
 * http://blog.csdn.net/hintcnuie/article/details/23934895
 */
public class CustomXWPFDocument extends XWPFDocument  {
	public CustomXWPFDocument(InputStream in) throws IOException {    
        super(in);    
    }    

    /**  
     *   
     */    
    public CustomXWPFDocument() {    
        super();    
        // TODO Auto-generated constructor stub     
    }    

    /**  
     * @param pkg  
     * @throws IOException  
     */    
    public CustomXWPFDocument(OPCPackage pkg) throws IOException {    
        super(pkg);    
        // TODO Auto-generated constructor stub     
    }  // picAttch 图片后面追加的字符串 可以是空格  
    public void createPicture(XWPFParagraph paragraph,int id, int width, int height,String picAttch) {    
        final int EMU = 9525;    
        width *= EMU;    
        height *= EMU;    
//        String blipId = getAllPictures().get(id).getPackageRelationship()    
//                .getId();    

        String blipId = Integer.toString(getAllPictures().size()-1);
        
        CTInline inline = paragraph.createRun().getCTR()    
                .addNewDrawing().addNewInline();    
        paragraph.createRun().setText(picAttch);  
        String picXml = ""    
                + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"    
                + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"    
                + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"    
                + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""    
                + id    
                + "\" name=\"Generated\"/>"    
                + "            <pic:cNvPicPr/>"    
                + "         </pic:nvPicPr>"    
                + "         <pic:blipFill>"    
                + "            <a:blip r:embed=\""    
                + blipId    
                + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"    
                + "            <a:stretch>"    
                + "               <a:fillRect/>"    
                + "            </a:stretch>"    
                + "         </pic:blipFill>"    
                + "         <pic:spPr>"    
                + "            <a:xfrm>"    
                + "               <a:off x=\"0\" y=\"0\"/>"    
                + "               <a:ext cx=\""    
                + width    
                + "\" cy=\""    
                + height    
                + "\"/>"    
                + "            </a:xfrm>"    
                + "            <a:prstGeom prst=\"rect\">"    
                + "               <a:avLst/>"    
                + "            </a:prstGeom>"    
                + "         </pic:spPr>"    
                + "      </pic:pic>"    
                + "   </a:graphicData>" + "</a:graphic>";    

        // CTGraphicalObjectData graphicData =     
        inline.addNewGraphic().addNewGraphicData();    
        XmlToken xmlToken = null;    
        try {    
            xmlToken = XmlToken.Factory.parse(picXml);    
        } catch (XmlException xe) {    
            xe.printStackTrace();    
        }    
        inline.set(xmlToken);    
        // graphicData.set(xmlToken);     

        inline.setDistT(0);    
        inline.setDistB(0);    
        inline.setDistL(0);    
        inline.setDistR(0);    

        CTPositiveSize2D extent = inline.addNewExtent();    
        extent.setCx(width);    
        extent.setCy(height);    

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();    
        docPr.setId(id);    
        docPr.setName("图片" + id);    
        docPr.setDescr("");    
    }    
    
    public static void main(String[] args) throws InvalidFormatException, IOException {
    	//新建word文档
    	CustomXWPFDocument document=new CustomXWPFDocument();
    	FileInputStream fis = new FileInputStream(new File(".png"));
    	document.addPictureData(fis, XWPFDocument.PICTURE_TYPE_JPEG);   
    	document.createPicture(document.createParagraph(),document.getAllPictures().size()-1, 400, 400,"    ");
    	
    	//把word文档写到文件中
    	File newfile=new File("newfile.docx");
    	FileOutputStream fos = new FileOutputStream(newfile); 
    	document.write(fos);
	}
    
    
    /** 
     * 根据图片类型，取得对应的图片类型代码 
     * @param picType 
     * @return int 
     */   
    private static int getPictureType(String picType){   
        int res = CustomXWPFDocument.PICTURE_TYPE_PICT;   
        if(picType != null){   
            if(picType.equalsIgnoreCase("png")){   
                res = CustomXWPFDocument.PICTURE_TYPE_PNG;   
            }else if(picType.equalsIgnoreCase("dib")){   
                res = CustomXWPFDocument.PICTURE_TYPE_DIB;   
            }else if(picType.equalsIgnoreCase("emf")){   
                res = CustomXWPFDocument.PICTURE_TYPE_EMF;   
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){   
                res = CustomXWPFDocument.PICTURE_TYPE_JPEG;   
            }else if(picType.equalsIgnoreCase("wmf")){   
                res = CustomXWPFDocument.PICTURE_TYPE_WMF;   
            }   
        }   
        return res;   
    }  
}
