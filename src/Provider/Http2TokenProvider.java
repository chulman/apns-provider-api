package Provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.security.sasl.AuthenticationException;

import Provider.Http2API.Http2ClientInit;
import Provider.Http2API.WebToken.JwtTokenManager;
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

public class Http2TokenProvider {
	private String host = "api.development.push.apple.com";
	private int port = 443;

	private Http2ClientInit http2ClientInit = null;

	private Channel channel = null;
	private EventLoopGroup eventLoopGroup = null;
	private Bootstrap bootstrap = null;
	
	private String authToken;
	
	public Http2TokenProvider() {

		SslContext sslContext = null;
		try {
			sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

			eventLoopGroup = new NioEventLoopGroup(1);
			bootstrap = new Bootstrap();

			http2ClientInit = new Http2ClientInit(sslContext, eventLoopGroup);

			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(http2ClientInit);
		} catch (SSLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public ChannelFuture send(String deviceToken, String bundleID, String message) throws AuthenticationException {
		
		if(authToken == null) {
			throw new AuthenticationException("you must Make AuthToken before Send to Apns");
		}
		
		/** message setting */
		String payload = JsonUtils.parse(message);

		FullHttpRequest request = null;

		HttpHeaders httpHeaders = new DefaultHttpHeaders();
		httpHeaders.add("authorization", "bearer " + authToken);
		httpHeaders.add("apns-topic",bundleID);
		
		request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, 
				"https://" + host + "/3/device/" + deviceToken, Unpooled.copiedBuffer(payload.toString().getBytes()),
				new DefaultHttpHeaders(), httpHeaders);

		System.out.println(request.toString());
		ChannelFuture channelFuture = channel.writeAndFlush(request);
		return channelFuture;
	}
	
	public String createToken(String keyID, String teamID, String secret) {
		try {
			authToken = new JwtTokenManager().createToken(keyID, teamID, secret);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authToken;
	}
}
