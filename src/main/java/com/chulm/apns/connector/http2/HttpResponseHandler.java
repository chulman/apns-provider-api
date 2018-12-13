package com.chulm.apns.connector.http2;

import com.chulm.apns.format.apns.HttpResponse;
import com.chulm.apns.format.constants.ApnsBody;
import com.chulm.apns.format.constants.ApnsHeader;
import com.chulm.apns.format.constants.ApnsHttpsCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper mapper = new ObjectMapper();

    private EventLoopGroup eventLoopGroup;

    private Http2ResponseListener http2ResponseListener;

    public HttpResponseHandler(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {

        log.info("Apns Response Status : {}", msg.status().code());

        HttpResponse response =  new HttpResponse();
        response.setApnsHttpsCode(ApnsHttpsCode.valueOf(msg.status().code()));
        response.setApns_id(msg.headers().get(ApnsHeader.ID));


        ByteBuf byteBufMessage = msg.content();
        int size = byteBufMessage.readableBytes();

        byte[] byteMessage = new byte[size];
        for (int i = 0; i < size; i++) {
            byteMessage[i] = byteBufMessage.getByte(i);
        }

        String contents = new String(byteMessage);

        log.info("Apns Response Contents : {}", contents);

        if (msg.status().code() == 200) {

        } else if (msg.status().code() == 413) {
            JsonNode node = mapper.readTree(contents);
            String error = node.get(ApnsBody.REASON).textValue();
            String timeStamp = node.get(ApnsBody.TIMESTAMP).textValue();

            response.setError(error);
            response.setTimeStamp(timeStamp);
        } else {
            JsonNode node = mapper.readTree(contents);
            String error = node.get(ApnsBody.REASON).textValue();

            response.setError(error);

        }

        http2ResponseListener.onMessage(response);
    }

    public Http2ResponseListener getHttp2ResponseListener() {
        return http2ResponseListener;
    }

    public void setHttp2ResponseListener(Http2ResponseListener http2ResponseListener) {
        this.http2ResponseListener = http2ResponseListener;
    }
}
