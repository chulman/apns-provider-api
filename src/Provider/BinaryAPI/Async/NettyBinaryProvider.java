package Provider.BinaryAPI.Async;

import java.io.IOException;

import Provider.BinaryAPI.Frame;
import Utils.BinaryUtils;
import Utils.ConverterUtils;
import Utils.JsonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;

public class NettyBinaryProvider {

	private ChannelFuture cf;
	private EventLoopGroup eventLoopGroup;
	
	private int expireDate = 4;
	private int priority = 10;
	private int notificationID = 5;

	public void connect(String certPath, String certName, String password) throws InterruptedException {

		eventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				ChannelPipeline cp = sc.pipeline();
				cp.addLast("ssl", new SslHandler(new SslSetting(certPath, certName, password).getSslEngine()));
				cp.addLast("response", new Response_handler());
			}
		});

		cf = bootstrap.connect(BinaryUtils.SANDBOX_HOST, BinaryUtils.PORT).sync();
		
		System.out.println("connect:" + cf.channel().isOpen());
//		cf.channel().closeFuture().sync();
	}
	
	public void send(String message, String deviceToken) throws IOException {
		
		ByteBuf msg =  Unpooled.buffer(BinaryUtils.MAX_PAYLOAD_BYTES);
		String payload = JsonUtils.parse(message);
		
		System.err.println(payload);
		
		Frame frame = new Frame(deviceToken, payload, expireDate, priority, notificationID);
		frame.pack();
		
		msg.writeByte(BinaryUtils.COMMAND_NOTIFICATION);
		msg.writeBytes(ConverterUtils.intTo4ByteArray(frame.getLength()));
		msg.writeBytes(frame.getData());

		cf.channel().writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
	}
	
	public void close() {
		cf.channel().close();
		eventLoopGroup.shutdownGracefully();
	}

	public int getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(int expireDate) {
		this.expireDate = expireDate;
	}

	public int getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
}
