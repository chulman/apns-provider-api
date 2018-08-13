package Provider.BinaryAPI.Async;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Response_handler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Response Handler:" + msg);
		ByteBuf buf =(ByteBuf) msg;	
		byte[] readByte = new byte[buf.readableBytes()];
			
		buf.readBytes(readByte);
	}

}















