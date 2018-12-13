package com.chulm.apns.connector.http2;

import com.chulm.apns.connector.ApnsConnector;
import com.chulm.apns.exception.AuthenticationException;
import com.chulm.apns.format.payload.Payload;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Http2Connector extends ApnsConnector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Http2ClientInitializer http2ClientInitializer = null;

    private Channel channel = null;
    private EventLoopGroup eventLoopGroup = null;
    private Bootstrap bootstrap = null;

    private  Http2ResponseListener http2ResponseListener;


    public Http2Connector() throws SSLException {

        SslContext sslContext = null;
        try {

            sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();


            eventLoopGroup = new NioEventLoopGroup(1);
            bootstrap = new Bootstrap();

            http2ClientInitializer = new Http2ClientInitializer(sslContext, eventLoopGroup);

            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(http2ClientInitializer);

        } catch (SSLException e) {
            e.printStackTrace();
            log.error("Http2 init ssl Exception. {}", e);
            throw e;
        }
    }

    @Override
    public boolean connect() throws Exception {
        ChannelFuture cf = null;
        cf = bootstrap.connect(host, port).sync();
        channel = cf.channel();

        log.info("connected: {}, end-point: {}", cf.isSuccess(), cf.channel().remoteAddress());

        return cf.isSuccess();
    }

    @Override
    public void close() throws IOException {
        channel.close();
        eventLoopGroup.shutdownGracefully();
    }

    public ChannelFuture send(String deviceToken, Payload payload, Map<String, Object> headerMap) throws AuthenticationException {

        FullHttpRequest request = null;

        String paylaod = payload.parseToJson();

        HttpHeaders httpHeaders = new DefaultHttpHeaders();

        Set<String> headerSet = headerMap.keySet();

        Iterator<String> iter  = headerSet.iterator();

        synchronized (headerMap){
            while(iter.hasNext()){
                String key = iter.next();
                Object value = headerMap.get(key);
                httpHeaders.add(key, value);
            }
        }

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                "https://" + host + "/3/device/" + deviceToken, Unpooled.copiedBuffer(paylaod.getBytes()),
                new DefaultHttpHeaders(), httpHeaders);

        ChannelFuture channelFuture = channel.writeAndFlush(request);
        return channelFuture;
    }

    public Http2ResponseListener getHttp2ResponseListener() {
        return http2ResponseListener;
    }

    public void setHttp2ResponseListener(Http2ResponseListener http2ResponseListener) {
        this.http2ResponseListener = http2ResponseListener;
        http2ClientInitializer.setHttp2ResponseListener(http2ResponseListener);
    }
}
