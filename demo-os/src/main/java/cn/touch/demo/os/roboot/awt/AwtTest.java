package cn.touch.demo.os.roboot.awt;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class AwtTest {

	public static void openITSys(String[] args) throws InterruptedException, AWTException {

        // 在新的浏览器窗口或在已有的浏览器窗口打开指定的URL(JDK 1.6以上)
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = URI.create("http://192.168.1.105");
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        final Robot rb = new Robot();
        rb.delay(3000);
        
        Common.pressKeys(rb, new int[]{KeyEvent.VK_TAB}, 500);
        
        
        Common.pressKeys(rb, new int[]{KeyEvent.VK_H,KeyEvent.VK_C,KeyEvent.VK_Q,KeyEvent.VK_TAB}, 500);
        
        Common.pressKeys(rb, new int[]{KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,KeyEvent.VK_TAB}, 500);
        
        Common.pressKeys(rb, new int[]{KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_ENTER}, 500);
        
        TimeUnit.SECONDS.sleep(15);
	}
	public static void typeConsole(String[] args) throws AWTException {
        final Robot rb = new Robot();

//        new Thread() {
//            public void run() {
//                rb.delay(2000); // 模拟回车
//                rb.keyPress(KeyEvent.VK_ENTER);
//                rb.keyRelease(KeyEvent.VK_ENTER);
//            }
//        }.start();

        rb.delay(3000);
        

        // 设置开始菜单的大概位置
        int x = 40;
        int y = Toolkit.getDefaultToolkit().getScreenSize().height - 10; // 鼠标移动到开始菜单,
        rb.mouseMove(x, y);
        rb.delay(500);
        Common.clickLMouse(rb, x, y, 500);
        
        
        // 运行CMD命令cmd enter
        int[] ks = { KeyEvent.VK_C, KeyEvent.VK_M,
                KeyEvent.VK_D, KeyEvent.VK_ENTER,KeyEvent.VK_ENTER};
        Common.pressKeys(rb, ks, 500);
//        rb.mouseMove(400, 400);
//        Common.clickLMouse(rb, x, y, 500);
        
        rb.delay(500);
        

        
        // 运行DIR命令dir enter
        ks = new int[] { KeyEvent.VK_D, KeyEvent.VK_I, KeyEvent.VK_R,
                KeyEvent.VK_ENTER,KeyEvent.VK_ENTER
                };
        Common.pressKeys(rb, ks, 500);
        rb.delay(1000);

        // 运行CLS命令cls enter
        ks = new int[] { KeyEvent.VK_C, KeyEvent.VK_L, KeyEvent.VK_S,
                KeyEvent.VK_ENTER,KeyEvent.VK_ENTER };
        Common.pressKeys(rb, ks, 500);
        rb.delay(1000);

        // 运行EXIT命令exit enter
        ks = new int[] { KeyEvent.VK_E, KeyEvent.VK_X, KeyEvent.VK_I,
                KeyEvent.VK_T, KeyEvent.VK_ENTER,KeyEvent.VK_ENTER };
        Common.pressKeys(rb, ks, 500);
        rb.delay(1000);

        
        JOptionPane.showMessageDialog(null, "演示完毕!");
        
	}

}
