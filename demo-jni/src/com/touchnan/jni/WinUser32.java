/*
 * WinUser32.java.WinUser32.java
 * Jan 5, 2011
 */
package com.touchnan.jni;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.HANDLE;
import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.util.User32;

/**
 * Jan 5, 2011
 * 
 * @author <a href="mailto:touchnan@gmail.com">chengqiang.han</a>
 * 
 */
public class WinUser32 extends User32 {

    /**
     * @param args
     * @throws IllegalAccessException 
     * @throws NativeException 
     */
    public static void main(String[] args) throws NativeException, IllegalAccessException {
        // TODO Auto-generated method stub
        
//        HWND handle = WinUser32.GetActiveWindow();
        System.out.println(WinUser32.MessageBox(0, "dsd", "Test", 2));
        HWND handle = WinUser32.GetDesktopWindow();
        System.out.println(WinUser32.GetWindowText(handle));
        
//        System.out.println(handle.getValueFromPointer());
//        JNative GetActiveWindow = new JNative("User32.dll", "GetActiveWindow");
//        GetActiveWindow.setRetVal(Type.INT);
//        GetActiveWindow.invoke();
//        int ret = GetActiveWindow.getRetValAsInt();
//        GetActiveWindow.dispose();
//        return new HWND(ret);        
        
    }
    
//    public static HANDLE GetActiveWindow() {
//        JNative GetActiveWindow = new JNative(DLL_NAME, "GetActiveWindow");
//        GetActiveWindow.setRetVal(Type.INT);
//        GetActiveWindow.invoke();
//        HWND handle = new HWND(GetActiveWindow.getRetValAsInt());
//        GetActiveWindow.dispose();
//        return handle;
//    }
}
