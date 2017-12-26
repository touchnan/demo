/*
 * cn.pubinfo.xtsms.TestClient.java
 * Jun 17, 2014 
 */
package cn.pubinfo.xtsms;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.DecoderException;

import cn.pubinfo.xtsms.client.Client;
import cn.pubinfo.xtsms.serv.sender.XjqSender;
import cn.pubinfo.xtsms.sms.ConnectionException;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 17, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class TestClient {

    /**
     * @param args
     * @throws ConnectionException
     * @throws DecoderException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws ConnectionException, DecoderException, UnsupportedEncodingException {
        v();
    }

    private static void a() throws ConnectionException {
        XjqSender sender = new XjqSender();
        sender.startup();
        for (int i = 0; i < 5; i++) {
            SmsDto sms = new SmsDto();
            sms.setVfrom("15558005568");
            sms.setSendto("13588140124");
            sms.setContent("从来不知道你说的是哪个"+i);
            sms.setSendtime(new Date());
            sender.send(sms);
        }
        sender.shutdown();
    }

    private static void v() throws ConnectionException {
        Client client = new Client(new InetSocketAddress("localhost", 1101), 1000);
        client.conn();
        for (int i = 0; i < 200000; i++) {
            SmsDto sms = new SmsDto();
            sms.setVfrom("15558005568");
            sms.setSendto("13588140124");
            sms.setContent("从来不知道你说的是哪个" + i);
            sms.setSendtime(new Date());
            client.send(sms);
        }
        client.shutdown();
    }

}
