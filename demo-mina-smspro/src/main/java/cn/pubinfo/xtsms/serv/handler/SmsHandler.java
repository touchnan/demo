/*
 * cn.pubinfo.xtsms.serv.handler.SMSHandler.java
 * Jun 17, 2014 
 */
package cn.pubinfo.xtsms.serv.handler;

import java.util.Collection;

import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 17, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public interface SmsHandler {
    /**
     * 报告已发送列表(持久化已经发送消息)
     * 
     * @param smses
     */

    void reportSent(Collection<SmsDto> smses);

    /**
     * 备份待发送数据
     * 
     * @param smses
     */
    void backup2Send(Collection<SmsDto> smses);

    /**
     * 加载备份待发数据
     */
    void loadBackup2Send();

    /**
     * 报告状态信息(持久化消息状态)
     * 
     * @param smses
     */
    void reportStatus(Collection<SmsDto> smses);

    /**
     * 报告接收信息(持久化接收消息)
     * @param smses
     */
    void reportReceive(Collection<SmsDto> smses);
}
