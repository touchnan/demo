package cn.touch.demo.os.event.demo;

import java.awt.event.KeyEvent;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
 
public class Hrblive4JKeyHook {
    static {
        keyboardHook = new LowLevelKeyboardProc() {
 
            @Override
            // 该函数参数的意思参考：http://msdn.microsoft.com/en-us/library/windows/desktop/ms644985(v=vs.85).aspx
            public LRESULT callback(int nCode, WPARAM wParam,
                    KBDLLHOOKSTRUCT lParam) {
                int w = wParam.intValue();
 
                if (lParam.vkCode == 162
                        && (w == WinUser.WM_KEYDOWN || w == WinUser.WM_SYSKEYDOWN)) {
                    Hrblive4JKeyHook.b = !b;
                }
 
                if (Hrblive4JKeyHook.b) {
 
                    // 按下alt键时w=.WM_SYSKEYDOWN; 按下其他大部分键时w=WinUser.WM_KEYDOWN
                    if (w == WinUser.WM_KEYDOWN || w == WinUser.WM_SYSKEYDOWN) {
                        switch (lParam.vkCode) {
                         
                        default:
                            MySocket.keyDownList.add("0:" + lParam.vkCode);
                            System.out.println("down key: "
                                    + (char) lParam.vkCode);
 
                        }
 
                    } else if (w == WinUser.WM_KEYUP
                            || w == WinUser.WM_SYSKEYUP) {
                        switch (lParam.vkCode) {
                        case 37:
                            Hrblive4JKeyHook.left = false;
                            MySocket.keyDownList.add("1:37");
                            System.out.println("up key: left");
                            break;
                        case 38:
                            Hrblive4JKeyHook.up = false;
                            MySocket.keyDownList.add("1:38");
                            System.out.println("up key: up");
                            break;
                        case 39:
                            Hrblive4JKeyHook.right = false;
                            MySocket.keyDownList.add("1:39");
                            System.out.println("up key: right");
                            break;
                        case 40:
                            Hrblive4JKeyHook.down = false;
                            MySocket.keyDownList.add("1:40");
                            System.out.println("up key: down");
                            break;
                        case KeyEvent.VK_X:
                            MySocket.keyDownList.add("1:88");
                            System.out.println("up key: left");
                            Hrblive4JKeyHook.x = false;
                            break;
                        case KeyEvent.VK_Z:
                            MySocket.keyDownList.add("1:90");
                            System.out.println("up key: left");
                            Hrblive4JKeyHook.z = false;
                            break;
                        default:
                            /*
                             * System.out.println("up key: " + (char)
                             * lParam.vkCode);
                             */
                        }
                    }
                }
 
                return User32.INSTANCE.CallNextHookEx(keyboardHHK, nCode,
                        wParam, lParam.getPointer());
            }
        };
 
    }
 
    public static Boolean b = true;
    public static Boolean left = false;
    public static Boolean right = false;
    public static Boolean up = false;
    public static Boolean down = false;
    public static Boolean x = false;
    public static Boolean z = false;
 
    public static HHOOK keyboardHHK;// 键盘钩子的句柄
    public static LowLevelKeyboardProc keyboardHook;// 键盘钩子函数
 
    // 安装钩子
    public static void setHook() {
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
 
        keyboardHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL,
                keyboardHook, hMod, 0);
    }
 
    // 卸载钩子
    public static void unhook() {
        User32.INSTANCE.UnhookWindowsHookEx(keyboardHHK);
    }
 
    public static void main(String[] args) {
 
        setHook();
 
        int result;
        MSG msg = new MSG();
        // 消息循环
        // 实际上while循环一次都不执行，这些代码的作用我理解是让程序在GetMessage函数这里阻塞，不然程序就结束了。
        while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in GetMessage");
                unhook();
                break;
            } else {
                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);
            }
        }
        unhook();
    }
 
}
