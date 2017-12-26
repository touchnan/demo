/*
 * cn.touch.qrcode.QrCoder.java
 * Jan 25, 2014 
 */
package cn.touch.qrcode;

import com.barcodelib.barcode.QRCode;

/**
 * Jan 25, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class QrCoder {
    // 0 - Pixel, 1 - CM, 2 - Inch
    private static int uom = 0;
    private static int resolution = 72;
    private static float leftMargin = 80.000f;
    private static float rightMargin = 80.000f;
    private static float topMargin = 80.000f;
    private static float bottomMargin = 80.000f;

    // 0 - 0, 1 - 90, 2 - 180, 3 - 270
    private static int rotate = 0;
    private static float moduleSize = 5.000f;

    public static void main(String[] args) throws Exception {
        QRCode barcode = new QRCode();
        
        //barcode.setData("http://192.168.1.51:8080/ds/app?uid=111");
        barcode.setData("如果是中文，Ad8&3那又如何 ");
        barcode.setDataMode(QRCode.MODE_BYTE);
        barcode.setVersion(10);
        barcode.setEcl(QRCode.ECL_M);
        barcode.setUOM(uom);
        barcode.setModuleSize(moduleSize);
        barcode.setLeftMargin(leftMargin);
        barcode.setRightMargin(rightMargin);
        barcode.setTopMargin(topMargin);
        barcode.setBottomMargin(bottomMargin);
        barcode.setResolution(resolution);
        barcode.setRotate(rotate);
        barcode.renderBarcode("e://qrcode.gif");
    }
}
