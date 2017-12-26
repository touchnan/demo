/*
 * cn.touch.mina.Serv.java
 * Sep 4, 2013 
 */
package cn.learn.mina;

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

/**
 * Sep 4, 2013
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Serv {
    public static void main(String[] args) {
        // I/O Service
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        
        // I/O Filter Chain
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
            new TextLineCodecFactory(Charset.forName("UTF-8"))));
        
        // I/O Handle
        acceptor.setHandler(new TimeHandle());
        
        int port = 8080;
        try {
          acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }         
    }
    
    static class TimeHandle extends IoHandlerAdapter {
        /**
         * 当有异常发生时触发
         */
        @Override
        public void exceptionCaught(IoSession ssn, Throwable cause) {
            cause.printStackTrace();
            ssn.close(true);
        }

        /**
         * 有新连接时触发
         */
        @Override
        public void sessionOpened(IoSession ssn) throws Exception {
            System.out.println("session open for " + ssn.getRemoteAddress());
        }

        /**
         * 连接被关闭时触发
         */
        @Override
        public void sessionClosed(IoSession ssn) throws Exception {
            System.out.println("session closed from " + ssn.getRemoteAddress());
        }

        /**
         * 收到来自客户端的消息
         */
        public void messageReceived(IoSession ssn, Object msg) throws Exception {
            String ip = ssn.getRemoteAddress().toString();
            System.out.println("===> Message From " + ip + " : " + msg);
            ssn.write("Hello " + msg);
        }
    }
}
