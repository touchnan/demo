/*
 * May 11, 2014 
 */
package cn.pubinfo.xtsms.gateway.client;

import java.net.SocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.sms.ConnectionException;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class XjqClient {
    private final static Logger log = LoggerFactory.getLogger(XjqClient.class);

    private SocketAddress socketAddress;
    private int timeoutMillis = 0;
    private IoSession session = null;
    private IoConnector connector = null;
    private boolean ready = false;// null值比较性能较差,因此有了此状态变量

    public XjqClient(SocketAddress socketAddress) {
        this(socketAddress, 0);
    }

    public XjqClient(SocketAddress socketAddress, int timeoutMillis) {
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
                    connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolEncoderAdapter() {

                        @Override
                        public void encode(IoSession paramIoSession, Object paramObject, ProtocolEncoderOutput out)
                                throws Exception {
                            String value =  paramObject == null ? "" :(String)paramObject;
                            byte[] b = value.getBytes();
                            IoBuffer buf = IoBuffer.allocate(b.length).setAutoExpand(true);
                            buf.put(b);
                            buf.flip();
                            out.write(buf);
                        }
                    }, new ProtocolDecoderAdapter() {
                        @Override
                        public void decode(IoSession paramIoSession, IoBuffer paramIoBuffer,
                                ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception {
                            paramIoBuffer.flip();
                            paramProtocolDecoderOutput.write(new String(paramIoBuffer.array()));
                        }

                    }));
                    
//                    connector.getFilterChain().addLast("codec",
//                            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));


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
//                                String ip = ssn.getRemoteAddress().toString();
                                log.debug("===> Message From {} : {}", ip, msg);
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
                        log.error("Xjq客户端链接异常...", e);
                        throw new ConnectionException("Xjq客户端链接异常...", e);
                    }
                }
            }
        }
    }

    public void send(Object msg) throws ConnectionException {
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
            session.close(true);
            session = null;
        }
        if (connector != null) {
            connector.dispose();
            connector = null;
        }
    }

}
