package cn.touch.demo.jdkapi.nio;

import cn.touch.demo.jdkapi.bio.ServerPort;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by chengqiang.han on 2018/10/16.
 */
public class NioTimeServer extends ServerPort {
    public static void main(String[] args) {
        int port = getPort(args);

        /*-
         JDK底层在释放Selector时，自动释放所有些多路利用器注册的资源(channel 或者 pipe)
        */
        try (Selector selector = Selector.open();
            ServerSocketChannel acceptorSvr = ServerSocketChannel.open()) {

            acceptorSvr.configureBlocking(false);
            acceptorSvr.socket().bind(new InetSocketAddress(port));
//            acceptorSvr.socket().bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"),port));

            acceptorSvr.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port:"+ port);

            while (true) {
                System.out.println("loop selector!");
                int num = selector.select();
                System.out.println("num = " + num);

                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    System.out.println("key op = " + key.interestOps());
                    if (key.isValid()) {
                        if (key.isAcceptable()) {// 客户端接入
                            System.out.println("a new connection accepted!");
//                            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
//                            SocketChannel sc = ssc.accept();
                            SocketChannel sc = acceptorSvr.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);//监听客户端写入流
                        } else if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            int readSize = 0;
                            try {
                                readSize = sc.read(readBuffer);
                            } catch (IOException e) {
                                //对端链路非法关闭
                                e.printStackTrace();
                                key.cancel();
                                sc.close();
                            }

                            System.out.println("readSize = " + readSize);
                            if (readSize > 0 ) {
                                readBuffer.flip();
                                byte[] bytes = new byte[readBuffer.remaining()];
                                readBuffer.get(bytes);
                                String body = new String(bytes, "UTF-8");
                                System.out.println("The time server receive order : " + body);
                                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";

                                byte[] result = currentTime.getBytes();
                                ByteBuffer writeBuffer = ByteBuffer.allocate(result.length);
                                writeBuffer.put(result);
                                writeBuffer.flip();
                                sc.write(writeBuffer);
                            } else if (readSize<0){
                                //对端链路关闭
                                key.cancel();
                                sc.close();
                            } else {
                                //读到0字节，忽略
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("\"what a fuck\" = " + "what a fuck");
            e.printStackTrace();
        }
    }
}
