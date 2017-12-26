/*
 * May 11, 2014 
 */
package cn.pubinfo.xtsms.client;

import java.net.SocketAddress;
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

import cn.pubinfo.xtsms.sms.ConnectionException;
import cn.pubinfo.xtsms.sms.SmsDto;

import com.alibaba.fastjson.JSON;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Client {
    private final static Logger log = LoggerFactory.getLogger(Client.class);

    private SocketAddress socketAddress;
    private int timeoutMillis = 0;
    private IoSession session = null;
    private IoConnector connector = null;
    private boolean ready = false;// null值比较性能较差,因此有了此状态变量

    public Client(SocketAddress socketAddress) {
        this(socketAddress, 0);
    }

    public Client(SocketAddress socketAddress, int timeoutMillis) {
        super();
        this.socketAddress = socketAddress;
        this.timeoutMillis = timeoutMillis;
    }

    public void conn() throws ConnectionException {
        if (connector == null) {
            synchronized (this) {
                if (connector == null) {
                    // 创建一个非阻塞的客户端程序
                    connector = new NioSocketConnector();
                    // 设置链接超时时间
                    connector.setConnectTimeoutMillis(timeoutMillis > 0 ? timeoutMillis : 10000);
                    connector.getFilterChain().addLast("codec",
                            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

                    // 添加业务逻辑处理器类
                    connector.setHandler(new IoHandlerAdapter() {
                        /**
                         * 当有异常发生时触发
                         */
                        @Override
                        public void exceptionCaught(IoSession ssn, Throwable cause) {
                            log.error("===> exceptionCaught", cause);
                            ssn.close(true);
                        }

                        /**
                         * 有新连接时触发
                         */
                        @Override
                        public void sessionOpened(IoSession ssn) throws Exception {
                            if (log.isDebugEnabled()) {
                                log.debug("===> session open for {}", ssn.getRemoteAddress());
                            }
                        }

                        /**
                         * 连接被关闭时触发
                         */
                        @Override
                        public void sessionClosed(IoSession ssn) throws Exception {
                            if (log.isDebugEnabled()) {
                                log.debug("===> session closed from {}", ssn.getRemoteAddress());
                            }
                        }

                        /**
                         * 收到来自服务端的消息
                         */
                        public void messageReceived(IoSession ssn, Object msg) throws Exception {
                            if (log.isDebugEnabled()) {
                                String ip = ssn.getRemoteAddress().toString();
                                log.debug("===> Message From {} : {}", ip, msg);
                            }

                            if ("SHUTDOWN".equals((String) msg)) {
                                shutdown();// 服务器关闭,则关闭客户端连接
                            }
                        }
                    });
                }
            }
        }
        open();

        ready = true;
    }

    private void open() throws ConnectionException {
        if (session == null || session.isConnected()) {
            synchronized (this) {
                if (session == null || session.isConnected()) {
                    try {
                        ConnectFuture future = connector.connect(socketAddress);// 创建连接
                        future.awaitUninterruptibly();// 等待连接创建完成
                        session = future.getSession();// 获得session
                    } catch (Exception e) {
                        log.error("客户端链接异常...", e);
                        throw new ConnectionException("客户端链接异常...", e);
                    }
                }
            }
        }
    }

    public void send(SmsDto sms) throws ConnectionException {
        send(JSON.toJSONString(sms, false));
    }

    private void send(String msg) throws ConnectionException {
        if (ready) {
            // 注意保证ready为真的情况下session不为空
            if (session.isConnected()) {
                session.write(msg);// 发送消息
            } else {
                conn();// 视具体情况,重新连接,打开session
                send(msg);
            }
        } else {
            throw new ConnectionException("连接还没准备好");
        }
    }

    public void shutdown() {
        ready = false;
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
            session = null;
        }
        if (connector != null) {
            connector.dispose();
            connector = null;
        }
    }

}
