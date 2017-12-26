/*
 * cn.pubinfo.xtsms.serv.sender.SMSSender.java
 * Jun 17, 2014 
 */
package cn.pubinfo.xtsms.serv.sender;

import cn.pubinfo.xtsms.sms.ConnectionException;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 17, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public interface SmsSender {
    SmsDto send(SmsDto sms) throws ConnectionException;
    boolean deliverSms(SmsDto sms);
    boolean deliverStatus(SmsDto sms);
    void stopDeliver();
    void startDeliver();
    
    void startup();//启动连接
    
    /**
     * 
     */
    void shutdown();//关闭连接
}
