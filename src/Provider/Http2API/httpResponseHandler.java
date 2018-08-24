package Provider.Http2API;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

public class httpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse>{

	EventLoopGroup eventLoopGroup;
	public httpResponseHandler(EventLoopGroup eventLoopGroup) {
		this.eventLoopGroup = eventLoopGroup;
	}


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
		System.out.println("status:"+msg.status());
		System.out.println("code:" + msg.status().code());
		System.out.println(msg.toString());

		ByteBuf byteBufMessage =msg.content();
		int size = byteBufMessage.readableBytes();

		byte [] byteMessage = new byte[size];
		for(int i = 0 ; i < size; i++){
			byteMessage[i] = byteBufMessage.getByte(i);
		}

		String str = new String(byteMessage);

		System.out.println("error context:"+str);
		if(msg.status().code() == 200) {
			ctx.channel().close();
		}else {

		}
		ChannelFuture channelFuture = ctx.channel().close();
		channelFuture.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("message write isSuccess : " + future.isSuccess());
				eventLoopGroup.shutdownGracefully();
			}
		});
	}
}
