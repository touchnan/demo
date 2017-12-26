/**
 * 
 */
package cn.touch.demo.poi.word.pic;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Jul 25, 2017.
 */
public class W {
	//由于一次需要向word中添加多张图片，其中有图片存在重复，一开始使用的创建图片代码为：
	//xwpf.createPicture(xwpf.getAllPictures().size()-1, 80, 30,pargraph); 
	
//	public void createPicture(int id, int width, int height,XWPFParagraph paragraph) {  
//        final int EMU = 9525;  
//        width *= EMU;  
//        height *= EMU;  
//        String blipId = getAllPictures().get(id).getPackageRelationship().getId();  
//        CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();  
//
//        String picXml = ""  
//                + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"  
//                + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"  
//                + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"  
//                + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""  
//                + id  
//                + "\" name=\"Generated\"/>"  
//                + "            <pic:cNvPicPr/>"  
//                + "         </pic:nvPicPr>"  
//                + "         <pic:blipFill>"  
//                + "            <a:blip r:embed=\""  
//                + blipId  
//                + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"  
//                + "            <a:stretch>"  
//                + "               <a:fillRect/>"  
//                + "            </a:stretch>"  
//                + "         </pic:blipFill>"  
//                + "         <pic:spPr>"  
//                + "            <a:xfrm>"  
//                + "               <a:off x=\"0\" y=\"0\"/>"  
//                + "               <a:ext cx=\""  
//                + width  
//                + "\" cy=\""  
//                + height  
//                + "\"/>"  
//                + "            </a:xfrm>"  
//                + "            <a:prstGeom prst=\"rect\">"  
//                + "               <a:avLst/>"  
//                + "            </a:prstGeom>"  
//                + "         </pic:spPr>"  
//                + "      </pic:pic>"  
//                + "   </a:graphicData>" + "</a:graphic>";  
//
//        // CTGraphicalObjectData graphicData =   
//        inline.addNewGraphic().addNewGraphicData();  
//        XmlToken xmlToken = null;  
//        try {  
//            xmlToken = XmlToken.Factory.parse(picXml);  
//        } catch (XmlException xe) {  
//            xe.printStackTrace();  
//        }  
//        inline.set(xmlToken);  
//        inline.setDistT(0);  
//        inline.setDistB(0);  
//        inline.setDistL(0);  
//        inline.setDistR(0);  
//
//        CTPositiveSize2D extent = inline.addNewExtent();  
//        extent.setCx(width);  
//        extent.setCy(height);  
//
//        CTNonVisualDrawingProps docPr = inline.addNewDocPr();  
//        docPr.setId(id);  
//        docPr.setName("Picture" + id);  
//        docPr.setDescr("Generated");  
//    }  
	
	
//	String ind = xwpf.addPictureData(is, XWPFDocument.PICTURE_TYPE_GIF);
//	int id =  xwpf.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_GIF);
//	xwpf.createPicture(ind, id, 80, 30,pargraph); 
	
//	public void createPicture(String blipId, int id, int width, int height,XWPFParagraph paragraph) {  
//        final int EMU = 9525;  
//        width *= EMU;  
//        height *= EMU;  
//        //String blipId = getAllPictures().get(id).getPackageRelationship().getId();  
//        CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();  
//
//        String picXml = "" +  
//                "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +  
//                "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +  
//                "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +  
//                "         <pic:nvPicPr>" +  
//                "            <pic:cNvPr id=\"" + id + "\" name=\"Generated\"/>" +  
//                "            <pic:cNvPicPr/>" +  
//                "         </pic:nvPicPr>" +  
//                "         <pic:blipFill>" +  
//                "            <a:blip r:embed=\"" + blipId + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +  
//                "            <a:stretch>" +  
//                "               <a:fillRect/>" +  
//                "            </a:stretch>" +  
//                "         </pic:blipFill>" +  
//                "         <pic:spPr>" +  
//                "            <a:xfrm>" +  
//                "               <a:off x=\"0\" y=\"0\"/>" +  
//                "               <a:ext cx=\"" + width + "\" cy=\"" + height + "\"/>" +  
//                "            </a:xfrm>" +  
//                "            <a:prstGeom prst=\"rect\">" +  
//                "               <a:avLst/>" +  
//                "            </a:prstGeom>" +  
//                "         </pic:spPr>" +  
//                "      </pic:pic>" +  
//                "   </a:graphicData>" +  
//                "</a:graphic>";  
//
//        // CTGraphicalObjectData graphicData =   
//        inline.addNewGraphic().addNewGraphicData();  
//        XmlToken xmlToken = null;  
//        try {  
//            xmlToken = XmlToken.Factory.parse(picXml);  
//        } catch (XmlException xe) {  
//            xe.printStackTrace();  
//        }  
//        inline.set(xmlToken);  
//        inline.setDistT(0);  
//        inline.setDistB(0);  
//        inline.setDistL(0);  
//        inline.setDistR(0);  
//
//        CTPositiveSize2D extent = inline.addNewExtent();  
//        extent.setCx(width);  
//        extent.setCy(height);  
//
//        CTNonVisualDrawingProps docPr = inline.addNewDocPr();  
//        docPr.setId(id);  
//        docPr.setName("Picture" + id);  
//        docPr.setDescr("Generated");  
//    } 
}
