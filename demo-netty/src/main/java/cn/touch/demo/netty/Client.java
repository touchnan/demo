/**
 * 
 */
package cn.touch.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import org.jboss.logging.Logger;

/**
 * Dec 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 */
public class Client {
    private static final Logger log = Logger.getLogger(Client.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        connect();

        // connectSticky();//客户端收到一粘包响应
    }

    public static void connect() {

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline()
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new ChannelHandlerAdapter() {

                                        private int counter = 0;

                                        /*
                                         * (non-Javadoc)
                                         * 
                                         * @see
                                         * io.netty.channel.ChannelHandlerAdapter
                                         * # channelActive
                                         * (io.netty.channel.ChannelHandlerContext
                                         * )
                                         */
                                        @Override
                                        public void channelActive(
                                                ChannelHandlerContext ctx)
                                                throws Exception {
                                            byte[] req = ("QUERY TIME ORDER"+System.getProperty("line.separator"))
                                                    .getBytes();

                                            ByteBuf cmd = null;
                                            for (int i = 0; i < 100; i++) {
                                                cmd = Unpooled
                                                        .buffer(req.length);
                                                cmd.writeBytes(req);
                                                ctx.writeAndFlush(cmd);
                                            }
                                        }

                                        /*
                                         * (non-Javadoc)
                                         * 
                                         * @see
                                         * io.netty.channel.ChannelHandlerAdapter
                                         * # channelRead
                                         * (io.netty.channel.ChannelHandlerContext
                                         * , java.lang.Object)
                                         */
                                        @Override
                                        public void channelRead(
                                                ChannelHandlerContext ctx,
                                                Object msg) throws Exception {
                                            String body = (String) msg;
                                            log.infof(
                                                    "Now is:%s; the counter is : %d",
                                                    body, ++counter);
                                        }

                                        /*
                                         * (non-Javadoc)
                                         * 
                                         * @see
                                         * io.netty.channel.ChannelHandlerAdapter
                                         * # exceptionCaught
                                         * (io.netty.channel.ChannelHandlerContext
                                         * , java.lang.Throwable)
                                         */
                                        @Override
                                        public void exceptionCaught(
                                                ChannelHandlerContext ctx,
                                                Throwable cause)
                                                throws Exception {
                                            log.errorf("发生错误=====>", cause);
                                            ctx.close();
                                        }

                                    });
                        }

                    });

            ChannelFuture f = b.connect("localhost", Server.PORT).sync();// 发起异步连接操作

            f.channel().closeFuture().sync();// 等待客户端链路关闭
        } catch (InterruptedException e) {
            log.error(e);
            group.shutdownGracefully();
        }

    }

    public static void connectSticky() {

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ChannelHandlerAdapter() {

                                private int counter = 0;

                                /*
                                 * (non-Javadoc)
                                 * 
                                 * @see io.netty.channel.ChannelHandlerAdapter#
                                 * channelActive
                                 * (io.netty.channel.ChannelHandlerContext)
                                 */
                                @Override
                                public void channelActive(
                                        ChannelHandlerContext ctx)
                                        throws Exception {
                                    byte[] req = "QUERY TIME ORDER".getBytes();

                                    ByteBuf cmd = null;
                                    for (int i = 0; i < 100; i++) {
                                        cmd = Unpooled.buffer(req.length);
                                        cmd.writeBytes(req);
                                        ctx.writeAndFlush(cmd);
                                    }
                                }

                                /*
                                 * (non-Javadoc)
                                 * 
                                 * @see io.netty.channel.ChannelHandlerAdapter#
                                 * channelRead
                                 * (io.netty.channel.ChannelHandlerContext,
                                 * java.lang.Object)
                                 */
                                @Override
                                public void channelRead(
                                        ChannelHandlerContext ctx, Object msg)
                                        throws Exception {
                                    ByteBuf buf = (ByteBuf) msg;
                                    byte[] req = new byte[buf.readableBytes()];
                                    buf.readBytes(req);
                                    String body = new String(req, "UTF-8");
                                    log.infof("Now is:%s; the counter is : %d",
                                            body, ++counter);
                                }

                                /*
                                 * (non-Javadoc)
                                 * 
                                 * @see io.netty.channel.ChannelHandlerAdapter#
                                 * exceptionCaught
                                 * (io.netty.channel.ChannelHandlerContext,
                                 * java.lang.Throwable)
                                 */
                                @Override
                                public void exceptionCaught(
                                        ChannelHandlerContext ctx,
                                        Throwable cause) throws Exception {
                                    log.errorf("发生错误=====>", cause);
                                    ctx.close();
                                }

                            });
                        }

                    });

            ChannelFuture f = b.connect("localhost", Server.PORT).sync();// 发起异步连接操作

            f.channel().closeFuture().sync();// 等待客户端链路关闭
        } catch (InterruptedException e) {
            log.error(e);
            group.shutdownGracefully();
        }

    }

}
