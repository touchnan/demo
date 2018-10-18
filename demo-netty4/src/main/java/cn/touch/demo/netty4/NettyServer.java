package cn.touch.demo.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by chengqiang.han on 2018/10/10.
 *
 * https://www.toutiao.com/a6609515667714474503/?tt_from=weixin&utm_campaign=client_share&wxshare_count=1&timestamp=1539151386&app=news_article&utm_source=weixin&iid=45817183214&utm_medium=toutiao_android&group_id=6609515667714474503
 *
 * http://p98.pstatp.com/large/pgc-image/1538894796043b4cd5331a7
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.group(ctx.channel().eventLoop())//复用同一个EventLoopGroup，减少资源的使用和线程的切换，特别是服务端引导一个客户端连接时
                                .channel(NioSocketChannel.class)
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                                        System.out.println("Received data");

                                        //BAD, 将msg从整个ChannelPipline中走一遍，所有的handler都要经过，
                                        ctx.channel().writeAndFlush("");
                                        //GOOD 从当前的handler一起到pipline的尾部，调用更短
                                        ctx.writeAndFlush("");
                                        //为了减少pipline的长度，如果一个handler只需要使用一次，那么可以在使用之后，将其从pipline中remove
                                    }
                                });
                        ChannelFuture future = bootstrap.connect(new InetSocketAddress("0.0.0.0",80));
                        future.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                //do something
                            }
                        });
                    }
                });
        Channel channel = bootstrap.bind(new InetSocketAddress(8888)).sync().channel();

        //在EventLoop的支持线程外使用channel,直接放入channel所对应的EventLoop的执行队列
        //不要直接使用channel.writeAndFlush(data),会导致线程的切换。
        channel.eventLoop().submit(()->{
            channel.writeAndFlush("");
        });

        channel.closeFuture().sync();
    }

}
