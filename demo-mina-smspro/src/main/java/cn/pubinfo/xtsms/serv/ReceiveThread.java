/*
 * cn.pubinfo.xtsms.serv.ReceiveThread.java
 * Jun 18, 2014 
 */
package cn.pubinfo.xtsms.serv;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.IConstants;
import cn.pubinfo.xtsms.queue.QueueManager;
import cn.pubinfo.xtsms.serv.handler.SmsHandler;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 18, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ReceiveThread extends Thread {
    private final static Logger log = LoggerFactory.getLogger(ReceiveThread.class);
    private boolean running = true;
    private SmsHandler smsHandler;

    public ReceiveThread(SmsHandler smsHandler) {
        super();
        this.smsHandler = smsHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        while (running) {
            try {
                if (QueueManager.receiveQueue.isEmpty()) {
                    TimeUnit.MILLISECONDS.sleep(IConstants.THREAD_SEND_SLEEP_MILLISECONDS);
                } else {
                    drain2report();
                }
            } catch (InterruptedException e) {
                log.error("Take Queue error", e);
            }
        }
    }

    /**
     * 获取并报告状态信息
     */
    private void drain2report() {
        Collection<SmsDto> c = QueueManager.receiveQueue.drainTo();
        if (!c.isEmpty()) {
            // 报告发送消息
            smsHandler.reportReceive(c);
        }

    }

    public void shutdown() {
        this.running = false;
        
        while (!QueueManager.receiveQueue.isEmpty()) {
            drain2report();
        }
    }
}