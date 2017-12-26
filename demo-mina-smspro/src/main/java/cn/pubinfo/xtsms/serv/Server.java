/*
 * cn.pubinfo.main.server.Server.java
 * May 11, 2014 
 */
package cn.pubinfo.xtsms.serv;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.pubinfo.xtsms.IConstants;
import cn.pubinfo.xtsms.queue.QueueManager;
import cn.pubinfo.xtsms.sms.SmsDto;

import com.alibaba.fastjson.JSON;

/**
 * May 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Server {
    private final static Logger log = LoggerFactory.getLogger(Server.class);
    private IoAcceptor acceptor = null;
    
    public void start() throws IOException  {
//       acceptor = new NioSocketAcceptor();
       acceptor = new NioSocketAcceptor();
       //((NioSocketAcceptor) acceptor).setReuseAddress(true);
//      acceptor.getSessionConfig().setReadBufferSize(2048);
      //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

      // I/O Filter Chain
      acceptor.getFilterChain().addLast("logger", new LoggingFilter());
      acceptor.getFilterChain().addLast("codec",
              new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

      // I/O Handle
      acceptor.setHandler(new IoHandlerAdapter() {

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
           * 收到来自客户端的消息
           */
          public void messageReceived(IoSession ssn, Object msg) throws Exception {
                  if (log.isDebugEnabled()) {
                      String ip = ssn.getRemoteAddress().toString();
                      log.debug("===> Message Received From {} : {}", ip, msg);
                  }
                  if ("QUIT".equals((String) msg)) {//客户端关闭
                      ssn.close(false);
                  } else {
                      //queue.offer(msg);//根据具体需求，超出极限容量可以忽略
                      SmsDto dto =JSON.parseObject(((String) msg).trim(), SmsDto.class);
                      dto.setSubmitDate(new Date());
                      QueueManager.sendQueue.put(dto);
                      if (log.isDebugEnabled()) {
                          ssn.write("received");
                      }
                  }
          }
      });
      
      acceptor.bind(new InetSocketAddress(IConstants.MINA_PORT));
    }
    
    public void shutdown() {
        if (acceptor!=null && acceptor.isActive()) {
            
            Map<Long, IoSession> sessions = acceptor.getManagedSessions();
            for (IoSession session : sessions.values()) {
                session.write("SHUTDOWN");//通知客户端服务端要关闭
                session.close(false);//关闭客户端
            }
            
            acceptor.unbind();
            acceptor.getFilterChain().clear();
            acceptor.dispose();
            acceptor = null;
        }
    }

}
