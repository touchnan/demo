/*
 * PosAdaptor.java.PosAdaptor.java
 * Dec 29, 2010
 */
package cn.zj.pubinfo.jnative.pos.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

/**
 * Dec 29, 2010
 * 
 * @author <a href="mailto:touchnan@gmail.com">chengqiang.han</a>
 * 
 */
public class CardIO {
    private static Logger log = LoggerFactory.getLogger(CardIO.class);
    private static String exceptInfo = "invoke JNative [%s] method [%s] occurred error : ";

    protected boolean invokeNativeMethod(String dllName, String funcName,
            Type[] paramTypes, String[] paramValues) {
        JNative n = null;
        try {
            n = new JNative(dllName, funcName);
            n.setRetVal(Type.INT);
            if (paramTypes != null && paramValues != null) {
                if (paramTypes.length != paramValues.length) {
                    throw new RuntimeException();
                }
                for (int i = 0; i < paramTypes.length; i++) {
                    n.setParameter(i, paramTypes[i], paramValues[i]);
                }
            }
            n.invoke();
            return 0 != Integer.parseInt(n.getRetVal());
        } catch (NativeException e) {
            log.error(String.format(exceptInfo, dllName, funcName), e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error(String.format(exceptInfo, dllName, funcName), e);
            throw new RuntimeException(e);
        }
    }
}
