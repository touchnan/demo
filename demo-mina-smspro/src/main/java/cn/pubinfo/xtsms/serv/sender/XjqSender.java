/*
 * cn.pubinfo.xtsms.serv.sender.XjqSender.java
 * Jun 19, 2014 
 */
package cn.pubinfo.xtsms.serv.sender;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.IConstants;
import cn.pubinfo.xtsms.gateway.client.XjqClient;
import cn.pubinfo.xtsms.sms.ConnectionException;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 19, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class XjqSender implements SmsSender {
    private static final Logger log = LoggerFactory.getLogger(XjqSender.class);
    private XjqClient client;
    private AtomicLong id = null;
    private String fname = "xjqsenderid";

    public XjqSender() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#startup()
     */
    @Override
    public void startup() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new XjqClient(new InetSocketAddress(IConstants.XJQ_SENDER_IP, IConstants.XJQ_SENDER_PORT));
                }
            }
        }

        try {
            client.conn();
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }

        if (id == null) {
            synchronized (this) {
                if (id == null) {
                    try {
                        List<String> vs = FileUtils.readLines(new File(fname), Charset.forName("utf-8"));
                        if (vs != null && !vs.isEmpty()) {
                            id = new AtomicLong(Long.parseLong(vs.get(0)));
                        }
                    } catch (IOException e) {
                        id = new AtomicLong(1);
                    } catch (NumberFormatException e) {
                        id = new AtomicLong(1);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#send(cn.pubinfo.xtsms.sms.SmsDto)
     */
    @Override
    public SmsDto send(SmsDto sms) throws ConnectionException {
        // 0074 0001 13777093427 153667098 1 1 00020131333333363737353036340001013
        
        String v =
                String.format(" %s %s %s 0 0 %s", id.getAndIncrement(),
                        StringUtils.isBlank(sms.getVfrom()) ? "" : sms.getVfrom(), sms.getSendto(),
                        Hex.encodeHexString(sms.getContent().getBytes()));
        int count = 4 + v.length();
        String a = StringUtils.leftPad(Integer.toString(count), 4, "0")+v;
        if (log.isDebugEnabled()) {
            client.send(a);
        }
        client.send(a);
        return sms;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#shutdown()
     */
    @Override
    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
        if (id != null) {
            try {
                FileUtils.write(new File(fname), Long.toString(id.get()), false);
            } catch (IOException e) {
                // pass
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#deliverSms(cn.pubinfo.xtsms.sms.SmsDto)
     */
    @Override
    public boolean deliverSms(SmsDto sms) {
        // pass
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#deliverStatus(cn.pubinfo.xtsms.sms.SmsDto)
     */
    @Override
    public boolean deliverStatus(SmsDto sms) {
        // pass
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#stopDeliver()
     */
    @Override
    public void stopDeliver() {
        // pass
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.sender.SmsSender#startDeliver()
     */
    @Override
    public void startDeliver() {
        // pass
    }

}
