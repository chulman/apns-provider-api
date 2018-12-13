package com.chulm.apns.connector.tcp;


import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.connector.ApnsConnector;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.exception.SocketNotCreatedException;
import com.chulm.apns.format.apns.Frame;
import com.chulm.apns.format.apns.Response;
import com.chulm.apns.format.constants.StatusCode;
import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.utils.BinaryUtils;
import com.chulm.apns.utils.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class BinaryConnector extends ApnsConnector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private SSLSocket sSock;

    private OutputStream outputStream = null;
    private BufferedInputStream bis = null;

    public BinaryConnector() {
        super();
        log.info("Apns Connector is synchronous Binary Connector");
    }

    @Override
    public boolean connect() throws Exception {

        if (sslGenerator == null) {
            throw new ResourceNotFoundException("Ssl Generator Not Found.");
        }

        try {
            sSock = (SSLSocket) sslGenerator.getSslSocketFactory().createSocket(host, port);
        } catch (IOException e) {
            throw new SocketNotCreatedException("Ssl Socket Not Created." + e);
        }

        if (sSock == null) {
            throw new SocketNotCreatedException("Ssl Socket Not Created.");
        }

        String[] cipherSuites = sSock.getSupportedCipherSuites();
        sSock.setEnabledCipherSuites(cipherSuites);
        sSock.startHandshake();

        log.info("connected: {}, end-point: {}", sSock.isConnected(), sSock.getRemoteSocketAddress());

        return sSock.isConnected();
    }

    @Override
    public void close() throws IOException {
        try {
            if(outputStream != null){
                outputStream.close();
            }

            if(bis != null){
                bis.close();
            }

            if(sSock != null){
                sSock.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Socket 응답없으면 성공 처리.
     */
    private Response receive(){

        Response response = new Response();
        byte[] resp = new byte[6];


        try {
            sSock.setSoTimeout(2000);
            bis = new BufferedInputStream(sSock.getInputStream());
            bis.read(resp);

            log.info("response packet: {} ", Arrays.toString(resp));

            byte[] identifier = Arrays.copyOfRange(resp,2,7);

            response.setIdentifier(ConverterUtils.byteArrayToInt(identifier));
            response.setCommand((long)(resp[0]));
            response.setStatusCode(StatusCode.valueOf(resp[1]));

        }catch (SocketTimeoutException e) {
            log.info("not received response packet");
            response.setStatusCode(StatusCode.NoErrorsEncountered);
        }catch (IOException e) {
            response.setStatusCode(StatusCode.Unknown);
        }

        return response;
    }

    public Response send(String deviceToken, Payload payload) throws IOException {

        log.info("payload : {}" , payload.parseToJson().toString());

        Frame frame = new Frame(deviceToken,payload);
        frame.pack();

        try {
            outputStream = sSock.getOutputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(BinaryUtils.COMMAND_NOTIFICATION);
            baos.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
            baos.write(frame.getData());
            outputStream.write(baos.toByteArray());
            outputStream.flush();

            baos.close();
            log.info("send packet: {}" , ConverterUtils.toHexaString(baos.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = receive();
        response.setDeviceToken(deviceToken);
        return response;
    }

    public Response send(String deviceToken, Payload payload, int identifier) throws IOException {

        log.info("payload : {} , identifier : {}, expireDate : {}", payload.parseToJson().toString(), identifier);

        Frame frame = new Frame(deviceToken,payload,identifier);
        frame.pack();


        try {
            outputStream = sSock.getOutputStream();
            outputStream.write(BinaryUtils.COMMAND_NOTIFICATION);
            outputStream.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
            outputStream.write(frame.getData());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = receive();
        response.setDeviceToken(deviceToken);
        return response;
    }

    public Response send(String deviceToken, Payload payload, int identifier, int expireDate) throws IOException {

        log.info("payload : {} , identifier : {}, expireDate : {}", payload.parseToJson().toString(), identifier, expireDate);

        Frame frame = new Frame(deviceToken,payload,identifier,expireDate);
        frame.pack();

        try {
            outputStream = sSock.getOutputStream();
            outputStream.write(BinaryUtils.COMMAND_NOTIFICATION);
            outputStream.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
            outputStream.write(frame.getData());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = receive();
        response.setDeviceToken(deviceToken);
        return response;
    }

    public Response send(String deviceToken, Payload payload, int identifier, int expireDate, int priority) throws IOException {

        log.info("payload : {} , identifier : {}, expireDate : {}, priority : {}",
                                                                payload.parseToJson().toString(), identifier, expireDate, priority);

        Frame frame = new Frame(deviceToken,payload,identifier,expireDate,priority);
        frame.pack();

        try {
            outputStream = sSock.getOutputStream();
            outputStream.write(BinaryUtils.COMMAND_NOTIFICATION);
            outputStream.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
            outputStream.write(frame.getData());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Response response = receive();
        response.setDeviceToken(deviceToken);
        return response;
    }

    public void setSslGenerator(SslGenerator sslGenerator) {
        this.sslGenerator = sslGenerator;
    }
}
