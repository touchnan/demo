/**
 * 
 */
package cn.touch.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Dec 11, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 */
public class Client {
	private static final Logger log = LoggerFactory.getLogger(Client.class);

	private EventLoopGroup group;
	private Bootstrap bootstrap;
	private Channel channel;

	public static final int WRITE_WAIT_SECONDS = 20;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		for (int i=0;i<3;i++) {
			new Thread(() -> {
				Client client = new Client().connect().testSend();
				try {
					TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
					e.printStackTrace();
				}
				client.disConnect();
			}).start();
		}
		TimeUnit.SECONDS.sleep(15);
	}

	public Client() {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)// 保持长连接状态
				.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder()).addLast(new LineBasedFrameDecoder(128))
								.addLast(new StringDecoder()).addLast(new IdleStateHandler(Server.READ_WAIT_SECONDS,
										WRITE_WAIT_SECONDS, 0, TimeUnit.SECONDS))
								.addLast(new SimpleChannelInboundHandler<String>() {
									int counter = 0;

									@Override
									protected void messageReceived(ChannelHandlerContext ctx, String msg)
											throws Exception {
										log.info("Now is:{}; the counter is : {}", msg, ++counter);
									}
								});
					}
				});
	}

	public Client connect() {

		try {
			// 连接服务端
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			ChannelFuture future = bootstrap.connect("localhost", Server.PORT).sync();// 发起异步连接操作
			channel = future.channel();

			// channel.closeFuture().sync();// 此方法会阻塞，等待客户端链路关闭
			if (future.isSuccess()) {
				log.info("connect server success-" + channel.remoteAddress());

			}

		} catch (InterruptedException e) {
			log.error("connect server failed- locahost :{}", Server.PORT);
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			channel = null;
		}

		return this;

	}

	public Client disConnect() {
		if (channel != null && channel.isOpen()) {
			channel.close();
		}
		if (group == null) {
			group.shutdownGracefully();
		}
		return this;
	}

	public Client testSend() {
		byte[] req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();

		ByteBuf cmd = null;
		for (int i = 0; i < 100; i++) {
			cmd = Unpooled.buffer(req.length);
			cmd.writeBytes(req);
			channel.writeAndFlush(cmd);
		}
		return this;
	}

}
