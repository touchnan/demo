/**
 * 
 */
package com.touchnan.jni;

import java.io.File;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

/**
 * @author <a href="mailto:touchnan@gmail.com">chengqiang.han</a>
 * 
 */
public class Test {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws NativeException 
	 */
	public static void main(String[] args) throws NativeException, IllegalAccessException {
//	    System.setProperty("jnative.debug", "false");
	    
	    System.setProperty("jnative.debug", "true");
	    
		// TODO Auto-generated method stub
		// System.loadLibrary("ERTrans");
		// System.out.println(System.getProperty("java.library.path"));
		// System.load("C:/WINDOWS/system32/ERTrans.dll");
	    // System.out.println(seekCard());
	    
		System.out.println(setCardParamV2());
//		System.out.println(openReaderAfterParam());
//		System.out.println(closeReader());
	}
	
    private static final int seekCard() throws NativeException,
    IllegalAccessException {
        /*
         * CardType：传出卡类型，0 IC 卡；1 UIM 卡；2 ID 卡; 3 CPU 卡
CardSN：卡序列号缓冲区指针
BufSize：传入接收卡序列号缓冲区长度，预留20 字节，寻卡成功传出卡序列号字节长度。
IC卡、ID卡、CPU卡返回序列号为10位长，UIM卡序列号为16位长

         */
        JNative n = null;
        try {
            n = new JNative("ERTrans", "SeekCard");
            n.setRetVal(Type.INT);
            int i = 0;
            n.setParameter(i++, Type.INT, "0");
            
            Pointer p = new Pointer(MemoryBlockFactory.createMemoryBlock(10));
            n.setParameter(i++, p);
            n.setParameter(i++, Type.INT, "10");
            n.invoke();
            System.out.println(p.getAsString());
            p.dispose();
            return Integer.parseInt(n.getRetVal());
//          return n.getRetValAsInt();
        } finally {
            if (n != null)
                n.dispose();
        }
    }	
	
	private static final int setCardParamV2() throws NativeException,
	IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("ERTrans", "SetCardParamV2");
			n.setRetVal(Type.INT);
			int i = 0;
//			n.setParameter(i++, Type.STRING, "10293822");
//			n.setParameter(i++, Type.STRING, "906");
//			n.setParameter(i++, Type.INT, "21");
//			n.setParameter(i++, Type.INT, "115200");
//			n.setParameter(i++, Type.INT, "115200");
//			n.setParameter(i++, Type.INT, "115200");
			n.invoke();
			return Integer.parseInt(n.getRetVal());
//			return n.getRetValAsInt();
		} finally {
			if (n != null)
				n.dispose();
		}
	}	
	
	private static final int openReaderAfterParam() throws NativeException,
	IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("ERTrans.dll", "OpenReaderAfterParam");
			n.setRetVal(Type.INT);
			int i = 0;
			n.setParameter(i++, Type.INT, "0");
			n.setParameter(i++, Type.INT, "906");
			n.setParameter(i++, Type.INT, "21");
			n.setParameter(i++, Type.INT, "115200");
//			n.setParameter(i++, Type.INT, "115200");
//			n.setParameter(i++, Type.INT, "115200");
			n.invoke();
			return Integer.parseInt(n.getRetVal());
		} finally {
			if (n != null)
				n.dispose();
		}
	}
	
	private static final int closeReader() throws NativeException,
			IllegalAccessException {
		JNative n = null;
		try {
			n = new JNative("ERTrans.dll", "CloseReader");
			n.setRetVal(Type.INT);
//			int i = 0;
//			n.setParameter(i++, Type.STRING, ip);
//			n.setParameter(i++, Type.INT, "" + port);
//			n.setParameter(i++, Type.INT, "" + intrcpt);
			n.invoke();
			return Integer.parseInt(n.getRetVal());
		} finally {
			if (n != null)
				n.dispose();
		}
	}
}
