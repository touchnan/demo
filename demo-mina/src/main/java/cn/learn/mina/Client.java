/*
 * cn.learn.mina.Client.java
 * Sep 4, 2013 
 */
package cn.learn.mina;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * Sep 4, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Client {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int port = 8080;
        
        // TODO Auto-generated method stub
        // 创建一个非阻塞的客户端程序
        IoConnector connector = new NioSocketConnector();
        // 设置链接超时时间
        connector.setConnectTimeout(30000);
        // 添加过滤器
//        connector.getFilterChain().addLast(
//                "codec",
//                new ProtocolCodecFilter(new DemuxingProtocolCodecFactory () {
//                    
//                    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
//                        // TODO Auto-generated method stub
//                        return null;
//                    }
//                    
//                    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
//                        // TODO Auto-generated method stub
//                        return null;
//                    }
//                }));
        
        connector.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        
        // 添加业务逻辑处理器类
        connector.setHandler(new ClientHandler());
        IoSession session = null;
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress("localhost", port));// 创建连接
            future.awaitUninterruptibly();// 等待连接创建完成
            session = future.getSession();// 获得session

//            FilePiece piece = new FilePiece("D:\\Develop Libs Tar\\apache-mina-2.0.7-bin.zip", 0);

//            InfoRequest ir = new InfoRequest(piece);

            session.write("aaaaaaaaaaaaa");// 发送消息
            
            session.write("bbbbb");// 发送消息

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端链接异常...");
        }

        session.getCloseFuture().awaitUninterruptibly();// 等待连接断开
        connector.dispose();
    }
    static class ClientHandler extends IoHandlerAdapter {
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
         * 收到来自服务端的消息
         */
        public void messageReceived(IoSession ssn, Object msg) throws Exception {
            String ip = ssn.getRemoteAddress().toString();
            System.out.println("===> Message From " + ip + " : " + msg);
            ssn.close(false);
            ssn.write("得到服务端 " + msg);
        }
    }
}
