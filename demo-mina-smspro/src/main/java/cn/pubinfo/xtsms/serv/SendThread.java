/*
 * May 11, 2014 
 */
package cn.pubinfo.xtsms.serv;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.IConstants;
import cn.pubinfo.xtsms.queue.QueueManager;
import cn.pubinfo.xtsms.serv.handler.SmsHandler;
import cn.pubinfo.xtsms.serv.sender.SmsSender;
import cn.pubinfo.xtsms.sms.ConnectionException;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class SendThread extends Thread {
    private final static Logger log = LoggerFactory.getLogger(SendThread.class);
    private boolean running = true;
    private SmsSender sender;
    private SmsHandler smsHandler;

    public SendThread(SmsSender sender, SmsHandler smsHandler) {
        super();
        this.sender = sender;
        this.smsHandler = smsHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        smsHandler.loadBackup2Send();
        while (running) {
            try {
                if (QueueManager.sendQueue.isEmpty()) {
                    TimeUnit.MILLISECONDS.sleep(IConstants.THREAD_SEND_SLEEP_MILLISECONDS);
                } else {
                    drain2send();
                }
            } catch (InterruptedException e) {
                log.error("Take Queue error", e);
            }
        }
    }

    /**
     * 获取信息并发送
     */
    private void drain2send() {
        Collection<SmsDto> c = QueueManager.sendQueue.drainTo();

        if (!c.isEmpty()) {
            // 单条发送
            for (SmsDto dto : c) {
                try {
                    dto.setSendtime(new Date());
                    sender.send(dto);
                    dto.setNstat(IConstants.SEND_SUCC);
                } catch (ConnectionException e) {
                    dto.setNstat(IConstants.SEND_FAILED);
                }
            }
            // 报告发送消息
            smsHandler.reportSent(c);
        }

    }

    /**
     * 备份待发送
     */
    private void backup() {
        while (!QueueManager.sendQueue.isEmpty()) {
            Collection<SmsDto> c = QueueManager.sendQueue.drainTo();
            if (!c.isEmpty()) {
                smsHandler.backup2Send(c);
            }
        }
    }

    public void shutdown() {
        this.running = false;
        backup();
    }

}
