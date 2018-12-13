package com.chulm.apns.connector.tcp.async;

import com.chulm.apns.format.apns.Response;
import com.chulm.apns.format.constants.StatusCode;
import com.chulm.apns.utils.ConverterUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ResponseHandler extends ChannelInboundHandlerAdapter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private ResponseListener responseListener;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf =(ByteBuf) msg;
		byte[] readByte = new byte[buf.readableBytes()];


		Response response = new Response();
		buf.readBytes(readByte);

		log.info("response packet: {}, data{} ", Arrays.toString(readByte), new String(readByte));

		byte[] identifier = Arrays.copyOfRange(readByte, 2, 7);

		response.setIdentifier(ConverterUtils.byteArrayToInt(identifier));
		response.setCommand((long) (readByte[0]));
		response.setStatusCode(StatusCode.valueOf(readByte[1]));

		if(responseListener != null) {
			responseListener.onMessage(response);
		}

		ctx.channel().close();
	}

	public void setResponseListener(ResponseListener responseListener) {
		this.responseListener = responseListener;
	}
}















