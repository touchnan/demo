package cn.touch.demo.netty4;

import cn.touch.demo.jdkapi.bio.ServerPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by chengqiang.han on 2018/10/18.
 */
public class NettyTimeClient extends ServerPort {
    public static void main(String[] args) {
        int port = getPort(args);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder())
                                    .addLast((new StringEncoder()))
                                    .addLast(new SimpleChannelInboundHandler<String>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                            System.out.println("Now time is : " + msg);
                                            ch.close();//shutdown the client
                                        }
                                    });
                        }
                    });

            ChannelFuture connect = bootstrap.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port)).sync();
            connect.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.channel().writeAndFlush("QUERY TIME ORDER");
                }
            });

            /* closeFuture(),sync(),addListener(), 返回的是同一个futrue*/
            connect.channel().closeFuture().sync().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("the client shutdown");
                }
            });

//            connect.channel().close().sync().addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    System.out.println("the client shutdown");
//                }
//            });
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
