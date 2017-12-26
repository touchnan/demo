package cn.touch.demo.os.event.demo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
 
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.MSG;
 
public class MainApp extends JFrame implements HotkeyListener,
        IntellitypeListener {
    public static boolean power = false;
    /**
     * 
     */
    private static final long serialVersionUID = 6883855006079508035L;
    private static MainApp mainFrame;
 
    private final JButton btnRegisterHotKey = new JButton();
    private final JButton btnUnregisterHotKey = new JButton();
    private final JPanel bottomPanel = new JPanel();
    private final JPanel mainPanel = new JPanel();
    private final JPanel topPanel = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JTextArea textArea = new JTextArea();
 
    /**
     * Creates new form.
     */
    public MainApp() {
        textArea.setEnabled(false);
        initComponents();
 
    }
 
    /**
     * Main method to launch this application.
     * <p>
     * 
     * @param args
     *            any command line arguments
     */
    public static void main(String[] args) {
        System.out.println(new File(".").getAbsolutePath());
        if (JIntellitype.checkInstanceAlreadyRunning("远端键盘控制系统：http://www.hrblive.com")) {
            System.exit(1);
        }
        if (!JIntellitype.isJIntellitypeSupported()) {
            System.exit(1);
        }
 
        mainFrame = new MainApp();
        mainFrame.setTitle("远端键盘控制系统：http://www.hrblive.com");
        center(mainFrame);
        mainFrame.setVisible(true);
        mainFrame.initJIntellitype();
 
        JIntellitype.getInstance().registerHotKey('.', 0, '.');
         
        MySocket mySocket = null;
 
        try {
            String line = null;
            mySocket = new MySocket();
 
            while (true) {
                line = mySocket.readKey();
                if (line != null) {
                    mySocket.send(line);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mySocket.close();
        }
    }
 
    /**
     * (non-Javadoc)
     * 
     * @see com.melloware.jintellitype.HotkeyListener#onHotKey(int)
     */
    public void onHotKey(int aIdentifier) {
        if ('.' == aIdentifier) {
            if (!power) {
                btnRegisterHotKey_actionPerformed(null);
            } else {
                btnUnregisterHotKey_actionPerformed(null);
            }
        }
        // output("WM_HOTKEY message received " +
        // Integer.toString(aIdentifier));
    }
 
    /**
     * (non-Javadoc)
     * 
     * @see com.melloware.jintellitype.IntellitypeListener#onIntellitype(int)
     */
    public void onIntellitype(int aCommand) {
 
        switch (aCommand) {
        case JIntellitype.APPCOMMAND_BROWSER_BACKWARD:
            output("BROWSER_BACKWARD message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_BROWSER_FAVOURITES:
            output("BROWSER_FAVOURITES message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_BROWSER_FORWARD:
            output("BROWSER_FORWARD message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_BROWSER_HOME:
            output("BROWSER_HOME message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_BROWSER_REFRESH:
            output("BROWSER_REFRESH message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_BROWSER_SEARCH:
            output("BROWSER_SEARCH message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_BROWSER_STOP:
            output("BROWSER_STOP message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_LAUNCH_APP1:
            output("LAUNCH_APP1 message received " + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_LAUNCH_APP2:
            output("LAUNCH_APP2 message received " + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_LAUNCH_MAIL:
            output("LAUNCH_MAIL message received " + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK:
            output("MEDIA_NEXTTRACK message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
            output("MEDIA_PLAY_PAUSE message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK:
            output("MEDIA_PREVIOUSTRACK message received "
                    + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_MEDIA_STOP:
            output("MEDIA_STOP message received " + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_VOLUME_DOWN:
            output("VOLUME_DOWN message received " + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_VOLUME_UP:
            output("VOLUME_UP message received " + Integer.toString(aCommand));
            break;
        case JIntellitype.APPCOMMAND_VOLUME_MUTE:
            output("VOLUME_MUTE message received " + Integer.toString(aCommand));
            break;
        default:
            output("Undefined INTELLITYPE message caught "
                    + Integer.toString(aCommand));
            break;
        }
    }
 
    /**
     * Centers window on desktop.
     * <p>
     * 
     * @param aFrame
     *            the Frame to center
     */
    private static void center(JFrame aFrame) {
        final GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final Point centerPoint = ge.getCenterPoint();
        final Rectangle bounds = ge.getMaximumWindowBounds();
        final int w = Math.min(aFrame.getWidth(), bounds.width);
        final int h = Math.min(aFrame.getHeight(), bounds.height);
        final int x = centerPoint.x - (w / 2);
        final int y = centerPoint.y - (h / 2);
        aFrame.setBounds(x, y, w, h);
        if ((w == bounds.width) && (h == bounds.height)) {
            aFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        aFrame.validate();
    }
 
    /**
     * Method to register a hotkey using the RegisterHotKey Windows API call.
     * <p>
     * 
     * @param aEvent
     *            the ActionEvent fired.
     */
    private void btnRegisterHotKey_actionPerformed(ActionEvent aEvent) {
        btnRegisterHotKey.setEnabled(false);
        btnUnregisterHotKey.setEnabled(true);
        power = true;
        output("远程按键发送启动，状态：监听。");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Hrblive4JKeyHook.setHook();
 
                int result;
                MSG msg = new MSG();
                // 消息循环
                // 实际上while循环一次都不执行，这些代码的作用我理解是让程序在GetMessage函数这里阻塞，不然程序就结束了。
                while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
                    if (result == -1) {
                        System.err.println("error in GetMessage");
                        Hrblive4JKeyHook.unhook();
                        break;
                    } else {
                        User32.INSTANCE.TranslateMessage(msg);
                        User32.INSTANCE.DispatchMessage(msg);
                    }
                }
                Hrblive4JKeyHook.unhook();
 
            }
        });
        t.start();
    }
 
    /**
     * Method to unregister a hotkey using the UnregisterHotKey Windows API
     * call.
     * <p>
     * 
     * @param aEvent
     *            the ActionEvent fired.
     */
    private void btnUnregisterHotKey_actionPerformed(ActionEvent aEvent) {
        btnUnregisterHotKey.setEnabled(false);
        btnRegisterHotKey.setEnabled(true);
        Hrblive4JKeyHook.unhook();
        power = false;
        output("远程按键发送启动，状态：未监听。");
    }
 
    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        mainPanel.setLayout(new BorderLayout());
        topPanel.setBorder(new EtchedBorder(1));
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(new EtchedBorder(1));
        btnRegisterHotKey.setText("启动服务");
        btnRegisterHotKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnRegisterHotKey_actionPerformed(e);
            }
        });
        btnUnregisterHotKey.setText("关闭服务");
        btnUnregisterHotKey.setEnabled(false);
        btnUnregisterHotKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnUnregisterHotKey_actionPerformed(e);
            }
        });
        topPanel.add(btnRegisterHotKey);
        topPanel.add(btnUnregisterHotKey);
        scrollPane.getViewport().add(textArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
 
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                // don't forget to clean up any resources before close
                JIntellitype.getInstance().cleanUp();
                System.exit(0);
            }
        });
 
        this.getContentPane().add(mainPanel);
        this.pack();
        this.setSize(400, 200);
    }
 
    /**
     * Initialize the JInitellitype library making sure the DLL is located.
     */
    public void initJIntellitype() {
        try {
 
            // initialize JIntellitype with the frame so all windows commands
            // can
            // be attached to this window
            JIntellitype.getInstance().addHotKeyListener(this);
            JIntellitype.getInstance().addIntellitypeListener(this);
            output("远程按键发送启动，状态：未监听。");
        } catch (RuntimeException ex) {
            output("系统不满足使用条件，缺失dll，请咨询作者!");
        }
    }
 
    private void output(String text) {
        textArea.append(text);
        textArea.append("\n");
    }
 
}
