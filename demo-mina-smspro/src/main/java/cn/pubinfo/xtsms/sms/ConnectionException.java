/*
 * cn.pubinfo.xtsms.sms.DisConnectionException.java
 * Jun 17, 2014 
 */
package cn.pubinfo.xtsms.sms;

/**
 * Jun 17, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ConnectionException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -3072175871303163928L;

    /**
     * 
     */
    public ConnectionException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public ConnectionException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ConnectionException(Throwable cause) {
        super(cause);
    }

    
    
}
