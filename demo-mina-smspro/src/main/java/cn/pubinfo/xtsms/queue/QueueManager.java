/*
 * cn.pubinfo.xtsms.serv.QueueManager.java
 * Jun 18, 2014 
 */
package cn.pubinfo.xtsms.queue;


/**
 * Jun 18, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class QueueManager {
    public final static SmsQueue sendQueue = new SmsQueue();
    public final static SmsQueue receiveQueue = new SmsQueue();
    public final static SmsQueue statusQueue = new SmsQueue();
}
