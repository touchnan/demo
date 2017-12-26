/*
 * cn.pubinfo.xtsms.serv.sender.SgppSender.java
 * Jun 17, 2014 
 */
package cn.pubinfo.xtsms.serv.sender;

import cn.pubinfo.xtsms.queue.QueueManager;
import cn.pubinfo.xtsms.sms.ConnectionException;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 17, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class SmppSender implements SmsSender {
    private boolean deliver = true;

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#send(cn.pubinfo.xtsms.sms.SmsDto)
     */
    @Override
    public SmsDto send(SmsDto sms) throws ConnectionException {
        // TODO Auto-generated method stub

        // TODO 发送
        // dto.setContent(content)
        // dto.setMsgId(msgId);
        return sms;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#deliverSms(cn.pubinfo.xtsms.sms.SmsDto)
     */
    @Override
    public boolean deliverSms(SmsDto sms) {
        if (this.deliver) {
            return QueueManager.receiveQueue.put(sms);
        }
        return this.deliver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#deliverStatus(cn.pubinfo.xtsms.sms.SmsDto)
     */
    @Override
    public boolean deliverStatus(SmsDto sms) {
        if (this.deliver) {
            return QueueManager.statusQueue.put(sms);
        }
        return this.deliver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#stopDeliver()
     */
    @Override
    public void stopDeliver() {
        this.deliver = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#startDeliver()
     */
    @Override
    public void startDeliver() {
        this.deliver = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#shutdown()
     */
    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#startup()
     */
    @Override
    public void startup() {
        // TODO Auto-generated method stub

    }

}
