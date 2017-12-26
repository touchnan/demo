/*
 * cn.pubinfo.xtsms.queue.SmsQueue.java
 * Jun 18, 2014 
 */
package cn.pubinfo.xtsms.queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.IConstants;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 18, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class SmsQueue {
    private final static Logger log = LoggerFactory.getLogger(SmsQueue.class);
    private BlockingQueue<SmsDto> sendQueue;

    public SmsQueue() {
        super();
        this.sendQueue = new LinkedBlockingQueue<SmsDto>();
    }

    public boolean isEmpty() {
        return sendQueue.isEmpty();
    }

    public Collection<SmsDto> drainTo() {
        Collection<SmsDto> c = new ArrayList<SmsDto>();
        sendQueue.drainTo(c, IConstants.DRAIN_SIZE);
        return c;
    }

    public boolean addAll(List<SmsDto> smses) {
        return sendQueue.addAll(smses);
    }

    public boolean put(SmsDto dto) {
        try {
            sendQueue.put(dto);
            return true;
        } catch (InterruptedException e) {
            log.error("===> put2send", e);
        }
        return false;
    }
}
