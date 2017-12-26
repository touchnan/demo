/*
 * cn.pubinfo.mina.client.Client.java
 * May 11, 2014 
 */
package cn.pubinfo.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Client {
    private final static Logger log = LoggerFactory.getLogger(Client.class);
    
    private final String socketHost ="localhost";
    private final int port = 1234;
    private IoSession session = null;
    private IoConnector connector = null;
    

    private Client() {
        super();
    }

    public static Client getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final Client instance = new Client().conn();
    }

    private Client conn() {
        // 创建一个非阻塞的客户端程序
        connector = new NioSocketConnector();
        // 设置链接超时时间
        connector.setConnectTimeoutMillis(30000);
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // 添加业务逻辑处理器类
        connector.setHandler(new ClientHandler());
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress(socketHost, port));// 创建连接
            future.awaitUninterruptibly();// 等待连接创建完成
            session = future.getSession();// 获得session
        } catch (Exception e) {
            log.error("客户端链接异常...",e);
        }
        return this;
    }

    public void send(String msg) {
        if (session.isConnected()) {
            session.write(msg);// 发送消息
        } else {
            log.error("连接已经关闭!");
        }
    }

    public void closeSession() {
        if (session != null) {
            if (session.isConnected()) {
                if (log.isDebugEnabled()) {
                    log.debug("QUIT");
                }
                session.write("QUIT");
                // Wait until the conn ends.
                session.getCloseFuture().awaitUninterruptibly();
            }
            session.close(true);
        }
        connector.dispose();
    }

    static class ClientHandler extends IoHandlerAdapter {
        /**
         * 当有异常发生时触发
         */
        @Override
        public void exceptionCaught(IoSession ssn, Throwable cause) {
            log.error("exceptionCaught", cause);
            ssn.close(true);
        }

        /**
         * 有新连接时触发
         */
        @Override
        public void sessionOpened(IoSession ssn) throws Exception {
            if (log.isDebugEnabled()) {
                log.debug("session open for {}", ssn.getRemoteAddress());
            }
        }

        /**
         * 连接被关闭时触发
         */
        @Override
        public void sessionClosed(IoSession ssn) throws Exception {
            if (log.isDebugEnabled()) {
                log.debug("session closed from {}", ssn.getRemoteAddress());
            }
        }

        /**
         * 收到来自服务端的消息
         */
        public void messageReceived(IoSession ssn, Object msg) throws Exception {
            String ip = ssn.getRemoteAddress().toString();
            if (log.isDebugEnabled()) {
                log.debug("===> Message From {} : {}", ip, msg);
            }
        }
    }
}
