/*
 * cn.pubinfo.main.server.Server.java
 * May 11, 2014 
 */
package cn.pubinfo.main.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Server {
    private final static Logger log = LoggerFactory.getLogger(Server.class);
    private final static int PORT = 1234;

    public static void main(String[] args) {
        // I/O Service
        IoAcceptor acceptor = new NioSocketAcceptor();
//        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        // I/O Filter Chain
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        // I/O Handle
        acceptor.setHandler(new TimeHandle());
        try {
            acceptor.bind(new InetSocketAddress(PORT));
        } catch (IOException e) {
            log.error("启动Mina服务失败", e);
        }
    }

    static class TimeHandle extends IoHandlerAdapter {
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
         * 收到来自客户端的消息
         */
        public void messageReceived(IoSession ssn, Object msg) throws Exception {
            String ip = ssn.getRemoteAddress().toString();
            if (log.isDebugEnabled()) {
                log.debug("===> PUT QUEUE Message From {} : {}", ip, msg);
            }
            if ("QUIT".equals((String) msg)) {
                ssn.close(false);
            }
            Main.queue.put(msg);
//            Main.queue.offer(msg);//根据具体需求，超出极限容量可以忽略
            ssn.write("received!");
            // ssn.close(false);
        }
    }

//    public void destroy() {
//        if (null != acceptor) {
//            acceptor.unbind(socketAddres);
//            acceptor.getFilterChain().clear(); // 清空Filter chain，防止下次重新启动时出现重名错误
//            acceptor.dispose(); // 可以另写一个类存储IoAccept，通过spring来创建，这样调用dispose后也会重新创建一个新的。或者可以在init方法内部进行创建。
//            acceptor = null;
//            // System.exit(0); 将导致容器停止
//        }
//
//    }

}
