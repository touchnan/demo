package cn.touch.demo.netty4;

import cn.touch.demo.jdkapi.bio.ServerPort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

/**
 * Created by chengqiang.han on 2018/10/18.
 */
public class NettyTimeServer extends ServerPort {
    public static void main(String[] args) {
        int port = getPort(args);
        NioEventLoopGroup coreGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(coreGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
                            .addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                   // super.channelRead(ctx, msg);
    //                                ((ByteBuf) msg).release();
//                                    ReferenceCountUtil.release(msg);
                                    System.out.println("The time server receive order : " + msg);
                                    ctx.writeAndFlush("QUERY TIME ORDER".equalsIgnoreCase((String) msg) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER");
                                }
                            });

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.SO_KEEPALIVE,true);


            ChannelFuture future = bootstrap.bind(port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("The time server is start in port:"+ port);
                }
            }).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            future.channel().closeFuture().sync();


            //关闭
//            future.channel().close().sync().addListener(future1 -> {
//                System.out.println("The time server shutdown!");
//            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            coreGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
