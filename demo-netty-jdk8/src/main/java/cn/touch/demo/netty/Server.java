/**
 *
 */
package cn.touch.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Dec 11, 2014
 *
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 */
public class Server {

	private static final Logger log = LoggerFactory.getLogger(Server.class);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workGroup;

	private ChannelFuture channelFuture;
	private ServerBootstrap bootstrap;

	public static final int READ_WAIT_SECONDS = 20;

	public static final int PORT = 8001;

	public Server() {
		bossGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Server().start();
	}

	public void start() {
		try {
			bootstrap = new ServerBootstrap().group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 128).option(ChannelOption.TCP_NODELAY, true) // 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
					.childOption(ChannelOption.SO_KEEPALIVE, true)// 保持长连接状态
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IdleStateHandler(READ_WAIT_SECONDS, 0, 0)) // 25秒没有接收到客户端请求,为idle
									// .addLast(new
									// ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
									// .addLast(new ObjectEncoder())
									.addLast(new StringEncoder()).addLast(new LineBasedFrameDecoder(128))
									.addLast(new StringDecoder())
									// .addLiast(new ChannelHandlerAdapter(){})
									.addLast(new SimpleChannelInboundHandler<String>() {
										private int counter = 0;

										@Override
										protected void messageReceived(ChannelHandlerContext ctx, String msg)
												throws Exception {
											log.info("The time server receive order:{}; the counter is:{}", msg,
													++counter);
											String currentTime = "Query TIME ORDER".equalsIgnoreCase(msg)
													? (new Date().toString()) : "BAD ORDER";
											currentTime += System.getProperty("line.separator");
											ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
											ctx.channel().writeAndFlush(resp);
										}

									});
						}
					});
			log.info("netty server start to bind  {}", PORT);
			channelFuture = bootstrap.bind(PORT).sync();// 绑定端口,同步等待成功
			
			log.info("netty server startup on {}", PORT);
			channelFuture.channel().closeFuture().sync();// 此方法会阻塞, 等待服务端监听端口关闭
			
//			 if(channelFuture.isSuccess()){
//				 log.info("netty server startup on {}", PORT);
//			 }
		} catch (InterruptedException e) {
			log.error("netty server start failed ", e);
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}

	public void stop() {
		if (channelFuture != null) {
			Channel channel = channelFuture.channel();
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		log.info("netty server stop");
	}

}
