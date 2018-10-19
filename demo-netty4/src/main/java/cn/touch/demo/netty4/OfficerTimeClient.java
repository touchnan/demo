package cn.touch.demo.netty4;

import cn.touch.demo.jdkapi.bio.ServerPort;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Date;
import java.util.List;

/**
 * Created by chengqiang.han on 2018/10/19.
 *
 * https://netty.io/wiki/user-guide-for-4.x.html
 */
public class OfficerTimeClient extends ServerPort {
    public static void main(String[] args) {
        int port = getPort(args);

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TimeClientHandler());

                    // Dealing with a Stream-based Transport
                    ch.pipeline().addLast(new TimeDecoder()).addLast(new TimeClientHandlerRolution());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    static class TimeClientHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf m = (ByteBuf) msg;
            try {
                long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
                System.out.println(new Date(currentTimeMillis));
                ctx.close();
            } finally {
                m.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    /**
     Now let us get back to the TIME client example. We have the same problem here.
     A 32-bit integer is a very small amount of data, and it is not likely to be fragmented often. However,
     the problem is that it can be fragmented, and the possibility of fragmentation will increase as the traffic increases.

     The simplistic solution is to create an internal cumulative buffer and wait until all 4 bytes are received into the internal buffer.
     The following is the modified TimeClientHandler implementation that fixes the problem:
     */
    static class TimeClientHandlerRolution extends ChannelInboundHandlerAdapter {
        private ByteBuf buf;

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            buf = ctx.alloc().buffer(4);
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            buf.release();
            buf = null;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf m = (ByteBuf) msg;
            buf.writeBytes(m);
            m.release();
            if (buf.readableBytes() >= 4) {
                long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
                System.out.println(new Date(currentTimeMillis));
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }

    /**
     * Although the first solution has resolved the problem with the TIME client, the modified handler does not look that clean.
     * Imagine a more complicated protocol which is composed of multiple fields such as a variable length field.
     * Your ChannelInboundHandler implementation will become unmaintainable very quickly.
     *
     * As you may have noticed, you can add more than one ChannelHandler to a ChannelPipeline, and therefore,
     * you can split one monolithic ChannelHandler into multiple modular ones to reduce the complexity of your application.
     * For example, you could split TimeClientHandler into two handlers:
     *
     * TimeDecoder which deals with the fragmentation issue, and
     * the initial simple version of TimeClientHandler.
     * Fortunately, Netty provides an extensible class which helps you write the first one out of the box:
     */
    static class TimeDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            if (in.readableBytes() < 4) {
                return;
            }
            out.add(in.readBytes(4));
        }
    }

    /**
     * If you are an adventurous person, you might want to try the ReplayingDecoder which simplifies the decoder even more.
     * You will need to consult the API reference for more information though.
     */
/*    static class TimeDecoder extends ReplayingDecoder<Void> {
        @Override
        protected void decode(
                ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            out.add(in.readBytes(4));
        }
    }*/


    static class  TimeEncoder<T > extends MessageToByteEncoder<T> {
        @Override
        protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
            //out.write(msg);
        }
    }

}
