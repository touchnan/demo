package cn.touch.demo.aio;

import cn.touch.demo.bio.ServerPort;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chengqiang.han on 2018/10/16.
 */
public class AioTimeClient extends ServerPort {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        int port = getPort(args);

        try {
            AsynchronousSocketChannel asc = AsynchronousSocketChannel.open();
//            asc.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port));
            asc.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port), null, new CompletionHandler<Void, Void>() {
                @Override
                public void completed(Void result, Void attachment) {

                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    asc.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            System.out.println("read~~~~~");
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            try {
                                String body = new String(bytes, "utf-8");
                                System.out.println("Now is :" + body);
//                                latch.countDown();
                            } catch (UnsupportedEncodingException e) {
                                System.err.println("read error~~~~~~~~~~~~");
                                e.printStackTrace();
                            }

                            readBuffer.clear();
                            asc.read(readBuffer,readBuffer,this);
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            exc.printStackTrace();
                        }
                    });

                    AtomicInteger countor = new AtomicInteger(3);
                    byte[] req = "QUERY TIME ORDER".getBytes();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
                    writeBuffer.put(req);
                    writeBuffer.flip();
                        asc.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer byteBuffer) {
                                if (byteBuffer.hasRemaining()) {
                                    asc.write(byteBuffer, byteBuffer, this);
                                } else {
                                    writeBuffer.rewind();
                                    int a = countor.getAndDecrement();
                                    System.out.println("a:"+a);
                                    if (a>0) {
                                        try {
                                            TimeUnit.SECONDS.sleep(1);//模拟连接发送命令，延迟几秒，因没做粘包处理
                                            System.out.println("wake up, wake up, wake up!");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        asc.write(writeBuffer, writeBuffer, this);
                                    } else {
                                        try {
                                            TimeUnit.SECONDS.sleep(1);//等接收完成后再关闭
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        latch.countDown();
                                    }
//                                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
//                                asc.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
//                                    @Override
//                                    public void completed(Integer result, ByteBuffer buffer) {
//                                        buffer.flip();
//                                        byte[] bytes = new byte[buffer.remaining()];
//                                        buffer.get(bytes);
//                                        try {
//                                            String body = new String(bytes, "utf-8");
//                                            System.out.println("Now is :" + body);
//                                            latch.countDown();
//                                        } catch (UnsupportedEncodingException e) {
//                                            System.err.println("read error~~~~~~~~~~~~");
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void failed(Throwable exc, ByteBuffer attachment) {
//                                        exc.printStackTrace();
//                                    }
//                                });
                                }
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                System.err.println("write error~~~~~~~~~~~~");
                                exc.printStackTrace();
                            }
                        });

                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.err.println("connect error~~~~~~~~~~~~");
                    exc.printStackTrace();
                    try {
                        asc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
}
