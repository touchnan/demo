/**
 * 
 */
package cn.touch.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.Date;

import org.jboss.logging.Logger;

/**
 * Dec 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 */
public class Server {

    private static final Logger log = Logger.getLogger(Server.class);
    public static final int PORT = 8001;

    /**
     * @param args
     */
    public static void main(String[] args) {
        start();

        // startSticky();//服务器收到两粘包请求
    }

    public static void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

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
                                                    "The time server receive order:%s; the counter is:%d",
                                                    body, ++counter);
                                            String currentTime = "Query TIME ORDER"
                                                    .equalsIgnoreCase(body) ? (new Date()
                                                    .toString()) : "BAD ORDER";
                                            currentTime += System
                                                    .getProperty("line.separator");
                                            ByteBuf resp = Unpooled
                                                    .copiedBuffer(currentTime
                                                            .getBytes());
                                            ctx.writeAndFlush(resp);
                                        }

                                        /*
                                         * (non-Javadoc)
                                         * 
                                         * @see
                                         * io.netty.channel.ChannelHandlerAdapter
                                         * # channelReadComplete
                                         * (io.netty.channel
                                         * .ChannelHandlerContext)
                                         */
                                        @Override
                                        public void channelReadComplete(
                                                ChannelHandlerContext ctx)
                                                throws Exception {// 处理过粘包问题，读完数据流
                                            log.info("flush on channelReadComplete ==============>");
                                            ctx.flush();
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

            ChannelFuture f = b.bind(PORT).sync();// 绑定端口,同步等待成功

            f.channel().closeFuture().sync();// 等待服务端监听端口关闭
        } catch (InterruptedException e) {
            log.error(e);
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void startSticky() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ChannelHandlerAdapter() {

                                private int counter = 0;

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
                                    log.infof(
                                            "The time server receive order:%s; the counter is:%d",
                                            body, ++counter);
                                    String currentTime = "Query TIME ORDER"
                                            .equalsIgnoreCase(body) ? (new Date()
                                            .toString()) : "BAD ORDER";
                                    ByteBuf resp = Unpooled
                                            .copiedBuffer(currentTime
                                                    .getBytes());
                                    ctx.writeAndFlush(resp);
                                }

                                /*
                                 * (non-Javadoc)
                                 * 
                                 * @see io.netty.channel.ChannelHandlerAdapter#
                                 * channelReadComplete
                                 * (io.netty.channel.ChannelHandlerContext)
                                 */
                                @Override
                                public void channelReadComplete(
                                        ChannelHandlerContext ctx)
                                        throws Exception {// 未处理粘包问题时，好像读到缓冲区满就算读完一次
                                    log.info("flush on channelReadComplete ==============>");
                                    ctx.flush();
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

            ChannelFuture f = b.bind(PORT).sync();// 绑定端口,同步等待成功

            f.channel().closeFuture().sync();// 等待服务端监听端口关闭
        } catch (InterruptedException e) {
            log.error(e);
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

}
