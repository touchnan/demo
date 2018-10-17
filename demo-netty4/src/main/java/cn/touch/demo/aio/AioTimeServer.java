package cn.touch.demo.aio;

import cn.touch.demo.bio.ServerPort;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * Created by chengqiang.han on 2018/10/16.
 */
public class AioTimeServer extends ServerPort {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        int port = getPort(args);
//        try (AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open()) {
//          此处不能用try-with-resources语句，因为这是异步进程，执行下去就把AsynchronousServerSocketChannel关闭了，会报错
        try  {
            AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open();
            assc.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port:"+ port);

            assc.accept(new String(""), new CompletionHandler<AsynchronousSocketChannel, Object>() {
                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {

                    /*-
                     当我们调用AsychronousServerSocketChannel的accept方法后，
                     如果有新的客户连接接入，系统将回调我们传入的CompletionHandler实例的completed方法，
                     表示新的客户端连接已经接入成功，因为一个AsychronousServerSocketChannel可以接入成千上万个客户端，
                     所以我们需要继续调用它的accept方法，接收其他的客户端连接，最终形成一个循环。
                     每当接收一个客户读连接成功之后，再异步接收新的客户端连接
                    */
                    assc.accept(null,this);

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    result.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer status, ByteBuffer attachment) {
                            if (status<=0) return;
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
//                            String body = new String(bytes, "UTF-8");
                            String body = new String(bytes);
                            System.out.println("The time server receive order : " + body);
                            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";

                            interactiveWrite(result, currentTime);
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            exc.printStackTrace();
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.err.println("accept error");
                    exc.printStackTrace();
                    latch.countDown();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void interactiveWrite(AsynchronousSocketChannel sc , String currentTime){
        byte[] result = currentTime.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(result.length);
        writeBuffer.put(result);
        writeBuffer.flip();
        sc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                // 如果没有发送完成，继续发送
                if (attachment.hasRemaining()) {
                    sc.write(attachment, attachment, this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
