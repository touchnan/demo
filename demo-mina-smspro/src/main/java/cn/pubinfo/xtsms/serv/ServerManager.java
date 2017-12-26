/*
 * cn.pubinfo.xtsms.serv.ServerManager.java
 * Jun 16, 2014 
 */
package cn.pubinfo.xtsms.serv;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.serv.handler.SmsDBHandler;
import cn.pubinfo.xtsms.serv.handler.SmsHandler;
import cn.pubinfo.xtsms.serv.sender.SmsSender;
import cn.pubinfo.xtsms.serv.sender.XjqSender;

/**
 * Jun 16, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class ServerManager {
    private final static Logger log = LoggerFactory.getLogger(ServerManager.class);

    private Server server;
    private DB db;

    private SendThread sendThread;
    private StatusThread statusThread;
    private ReceiveThread receiveThread;
    
    private SmsSender smsSender;
    private SmsHandler smsHandler;

    public void start() {
        db = new DB();// 初始化数据库

        // 后台线程启动
        //smsSender = new SmppSender();
        smsSender = new XjqSender();
        smsSender.startup();
        
        smsHandler = new SmsDBHandler(db);
        sendThread = new SendThread(smsSender, smsHandler);//发送线程
        sendThread.start();
        statusThread=new StatusThread(smsHandler);//状态更新线程
        statusThread.start();
        receiveThread = new ReceiveThread(smsHandler);//接收信息线程
        receiveThread.start();
        
        
        smsSender.startDeliver();//默认开启接收
        
        server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            log.error("启动错误", e);
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        server.shutdown();
        
        // 后台线程关闭
        smsSender.stopDeliver();//关闭接收
        sendThread.shutdown();
        statusThread.shutdown();
        receiveThread.shutdown();

        if (db != null) {
            db.shutdown();
        }
        
        smsSender.shutdown();//连接关闭
    }

}
