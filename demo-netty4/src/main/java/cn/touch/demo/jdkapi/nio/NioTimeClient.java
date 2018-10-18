package cn.touch.demo.jdkapi.nio;

import cn.touch.demo.jdkapi.bio.ServerPort;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by chengqiang.han on 2018/10/16.
 */
public class NioTimeClient extends ServerPort {
    public static void main(String[] args) {
        int port = getPort(args);

        /*-
         JDK底层在释放Selector时，自动释放所有些多路利用器注册的资源(channel 或者 pipe)
        */
        try (Selector selector = Selector.open(); SocketChannel sc = SocketChannel.open()) {
            sc.configureBlocking(false);
            sc.socket().setReuseAddress(true);
//            sc.socket().setReceiveBufferSize();
//            sc.socket().setSendBufferSize();

            /*-
                首先对SocketChannel的connect进行操作，如果连接成功，则将SocketChannel注册到多路利用器Selector上
                注册SelectionKey.OP_READ,如果没有直接连接成功，则说明服务端没有返回TCP握手应答消息，但这并不代表连接失败，
                我们需要将SocketChannel注册到多路复用器Selector上，注册SelectionKey.OP_CONNECT，当服务端返回TCP syn-ack消息
                后，Selectior就能够轮询这个SocketChannel处于连接就绪状态。
             */
            boolean connect = sc.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
            if (connect) {
                sc.register(selector, SelectionKey.OP_READ);
                interactive(sc);
            } else {
                sc.register(selector, SelectionKey.OP_CONNECT);
            }
            boolean stop = false;
            while (!stop) {
                System.out.println("loop selector!");
                int num = selector.select(1000);
                System.out.println("num = " + num);

                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    System.out.println("key op  = " + key.interestOps());
                    if (key.isValid()) {
                        if (key.isConnectable()) {//服务器已经返回ACK应答消息
                            System.out.println("connection ACK!");
                            if (sc.finishConnect()) {//客户端连接成功
                                sc.register(selector,SelectionKey.OP_READ);
                                interactive(sc);
                            } else {//客户端连接失败
                                System.exit(1);//退出进程
                            }
                        } else if (key.isReadable()) {

                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            int readSize = sc.read(readBuffer);
                            if (readSize > 0 ) {
                                readBuffer.flip();
                                byte[] bytes = new byte[readBuffer.remaining()];
                                readBuffer.get(bytes);
                                String body = new String(bytes, "UTF-8");
                                System.out.println("Now is : " + body);
                                stop = true;
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
            e.printStackTrace();
        }
    }

    private static void interactive(SocketChannel sc) throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            System.out.println("Send order 2 server succeed.");
        }
    }
}
