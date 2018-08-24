package Provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;

import Provider.Http2API.Http2ClientInit;
import Utils.JsonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class Http2CertProvider {
	
	private String alg 		= "sunx509";
	private String key_type 	= "PKCS12";
	private String host		= "api.development.push.apple.com";
	private int port 		= 443;

	private KeyManagerFactory keyManagerFactory = null;
	private Http2ClientInit http2ClientInit = null;

	private Channel channel = null;
	private EventLoopGroup eventLoopGroup = null;
	private Bootstrap bootstrap = null;
	
	
	public void setConfig(String certPath, String fileName, String pw) {
		InputStream inputStream = null;
		try {

			char[] password = pw.toCharArray();

			inputStream = new FileInputStream(new File(certPath+fileName));
			KeyStore keyStore = KeyStore.getInstance(key_type);
			keyStore.load(inputStream, password);

			keyManagerFactory = KeyManagerFactory.getInstance(alg);
			keyManagerFactory.init(keyStore, password);

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					System.out.println("inputStream close error." + e);
				}
			}

			try {
				SslContext sslContext = SslContextBuilder.forClient()
						.trustManager(InsecureTrustManagerFactory.INSTANCE)
						.keyManager(keyManagerFactory)
						.build();


				eventLoopGroup = new NioEventLoopGroup(1);
				bootstrap = new Bootstrap();

				http2ClientInit = new Http2ClientInit(sslContext, eventLoopGroup);

				bootstrap.group(eventLoopGroup)
				.channel(NioSocketChannel.class)
				.handler(http2ClientInit);

			} catch (SSLException e) {
				e.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e) {
			throw new IllegalStateException("create key manager factory failed");
		} 
	}

	public boolean connect() {
		ChannelFuture f = null;
		try {
			f = bootstrap.connect(host, port).sync();
			channel = f.channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return f.channel().isOpen();
	}

	public void close() {
		channel.close();
		eventLoopGroup.shutdownGracefully();
	}
	
	public ChannelFuture send(String deviceToken, String message) {
		/** message setting */
		String payload = JsonUtils.parse(message);

		FullHttpRequest request = null;

		HttpHeaders httpHeaders = new DefaultHttpHeaders();
		
		request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, 
				"https://" + host + "/3/device/" + deviceToken, Unpooled.copiedBuffer(payload.toString().getBytes()),
				new DefaultHttpHeaders(), httpHeaders);

		System.out.println(request.toString());
		ChannelFuture channelFuture = channel.writeAndFlush(request);
		return channelFuture;
	}

}
