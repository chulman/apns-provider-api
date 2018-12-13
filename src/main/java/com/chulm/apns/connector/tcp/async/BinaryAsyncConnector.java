package com.chulm.apns.connector.tcp.async;

import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.connector.ApnsConnector;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.format.apns.Frame;
import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.utils.BinaryUtils;
import com.chulm.apns.utils.ConverterUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BinaryAsyncConnector extends ApnsConnector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ChannelFuture cf;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    private ResponseHandler responseHandler;

    public BinaryAsyncConnector() {
        super();
        log.info("Apns Connector is asynchronous Binary Connector");
    }

    @Override
    public boolean connect() throws Exception {

        if (sslGenerator == null) {
            throw new ResourceNotFoundException("Ssl Generator Not Found.");
        }


        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        responseHandler = new ResponseHandler();

        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline cp = socketChannel.pipeline();
                        cp.addLast("ssl", new SslHandler(sslGenerator.getSslEngine()));
                        cp.addLast("response",responseHandler);
                    }
                });


        cf = bootstrap.connect(host, port).sync();


        log.info("connected: {}, end-point: {}", cf.isSuccess(), cf.channel().remoteAddress());

        return cf.isSuccess();
    }

    /**
     * if Close is faster than Send to Apns Server, Notification is not Send to Device.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        cf.channel().close();
        eventLoopGroup.shutdownGracefully();
    }



    public void send(String deviceToken, Payload payload, int identifier) throws IOException {

        log.info("payload : {} , identifier : {}, expireDate : {}", payload.parseToJson().toString(), identifier);

        Frame frame = new Frame(deviceToken,payload,identifier);
        frame.pack();


        ByteBuf msg =  Unpooled.buffer(BinaryUtils.MAX_PAYLOAD_BYTES);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(BinaryUtils.COMMAND_NOTIFICATION);
        baos.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
        baos.write(frame.getData());

        msg.writeBytes(baos.toByteArray());

        baos.close();

        cf.channel().writeAndFlush(msg);
    }

    public void send(String deviceToken, Payload payload, int identifier, int expireDate) throws IOException {

        log.info("payload : {} , identifier : {}, expireDate : {}", payload.parseToJson().toString(), identifier, expireDate);

        Frame frame = new Frame(deviceToken,payload,identifier,expireDate);
        frame.pack();


        ByteBuf msg =  Unpooled.buffer(BinaryUtils.MAX_PAYLOAD_BYTES);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(BinaryUtils.COMMAND_NOTIFICATION);
        baos.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
        baos.write(frame.getData());

        msg.writeBytes(baos.toByteArray());

        baos.close();

        cf.channel().writeAndFlush(msg);
    }

    public void send(String deviceToken, Payload payload, int identifier, int expireDate, int priority) throws IOException {

        log.info("payload : {} , identifier : {}, expireDate : {}, priority : {}", payload.parseToJson().toString(), identifier, expireDate, priority);

        Frame frame = new Frame(deviceToken,payload,identifier,expireDate,priority);
        frame.pack();

        ByteBuf msg =  Unpooled.buffer(BinaryUtils.MAX_PAYLOAD_BYTES);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(BinaryUtils.COMMAND_NOTIFICATION);
        baos.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
        baos.write(frame.getData());

        msg.writeBytes(baos.toByteArray());

        baos.close();

        cf.channel().writeAndFlush(msg);
    }

    /**
     *  When a response is received, the message is no longer transmitted.
     *
     * @param responseListener
     */
    public void setResponseListener(ResponseListener responseListener){
        responseHandler.setResponseListener(responseListener);
    }

    public void setSslGenerator(SslGenerator sslGenerator) {
        this.sslGenerator = sslGenerator;
    }

}
