package com.chulm.apns.connector.http2;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http2.DefaultHttp2Connection;
import io.netty.handler.codec.http2.DelegatingDecompressorFrameListener;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.HttpToHttp2ConnectionHandler;
import io.netty.handler.codec.http2.HttpToHttp2ConnectionHandlerBuilder;
import io.netty.handler.codec.http2.InboundHttp2ToHttpAdapterBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

public class Http2ClientInitializer extends ChannelInitializer<SocketChannel> {

	SslContext sslctx;
	int max_length = Integer.MAX_VALUE;

	private HttpToHttp2ConnectionHandler httpToHttp2ConnectionHandlerBuilder;
	private HttpResponseHandler responseHandler;

	private Http2ResponseListener http2ResponseListener;
	private EventLoopGroup eventLoopGroup;

	public Http2ClientInitializer(SslContext sslctx, EventLoopGroup eventLoopGroup) {
		super();
		this.sslctx = sslctx;
		this.eventLoopGroup = eventLoopGroup;
	}

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {

		final Http2Connection http2Connection = new DefaultHttp2Connection(false);
		httpToHttp2ConnectionHandlerBuilder = new HttpToHttp2ConnectionHandlerBuilder()
				.frameListener(new DelegatingDecompressorFrameListener(
						http2Connection,
						new InboundHttp2ToHttpAdapterBuilder(http2Connection)
						.maxContentLength(max_length)
						.propagateSettings(true)
						.build()))
				.connection(http2Connection)
				.build();
		responseHandler = new HttpResponseHandler(eventLoopGroup);

		ChannelPipeline pipeline = socketChannel.pipeline();

		SslHandler sslHandler = sslctx.newHandler( socketChannel.alloc() );

		pipeline.addLast(sslHandler);
		pipeline.addLast(httpToHttp2ConnectionHandlerBuilder);
		pipeline.addLast(responseHandler);

	}

	HttpResponseHandler responseHandler() {
		return responseHandler;
	}

	public Http2ResponseListener getHttp2ResponseListener() {
		return http2ResponseListener;
	}

	public void setHttp2ResponseListener(Http2ResponseListener http2ResponseListener) {
		this.http2ResponseListener = http2ResponseListener;
		responseHandler.setHttp2ResponseListener(http2ResponseListener);
	}
}
