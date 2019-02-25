package com.chulm.apns.provider;

import com.chulm.apns.auth.JwtProvider;
import com.chulm.apns.connector.http2.Http2Connector;
import com.chulm.apns.connector.http2.Http2ResponseListener;
import com.chulm.apns.exception.AuthenticationException;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.exception.TokenVerifyException;
import com.chulm.apns.format.constants.ApnsHeader;
import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.utils.Constants;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApnsProvider {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String host;
    private int port;

    private boolean production = false;
    private Http2Connector http2Connector;

    private String accessToken = null;

    private String keyID;
    private String teamID;
    private String secret;

    private Http2ResponseListener http2ResponseListener=null;

    private final JwtProvider jwtProvider = JwtProvider.getInstance();

    public ApnsProvider(boolean production){

        this.production = production;

        try {
            http2Connector = new Http2Connector();

        } catch (SSLException e) {
            log.error("SSLException Occured. {} ", e);
        }
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public void createToken(String keyID, String teamID, String secret) throws ResourceNotFoundException {

        if(keyID==null || teamID == null || secret == null){
            throw new ResourceNotFoundException("Apns Resource not found");
        }

        if(keyID.equals("") || teamID.equals("") | secret.equals("")){
            throw new ResourceNotFoundException("Apns Resource not found");
        }

        try {
            accessToken = jwtProvider.createToken(keyID, teamID, secret);
        } catch (Exception e) {
            log.error(" Create Token is fail." + e);
        }
    }

    public String refreshToken() throws Exception {
        if(keyID == null || teamID == null || secret == null){
            throw new ResourceNotFoundException("Token Resource not Found. retry create(...)");
        }
        accessToken = jwtProvider.createToken(keyID,teamID,secret);
        return accessToken;
    }

    private boolean validate(String accessToken) {

        boolean verify = false;
        try {
            verify =  jwtProvider.validate(accessToken);
        } catch (TokenVerifyException e) {
            e.printStackTrace();
            return false;
        }
        return verify;

    }

    public void connect(){

        if (production) {
            host = Constants.HTTP_PRODUCTION_HOST;
            port = Constants.HTTPS_PORT;

        } else {
            host = Constants.HTTP_DEVELPMENT_HOST;
            port = Constants.HTTPS_PORT;
        }

        http2Connector.setHost(host);
        http2Connector.setPort(port);

        try {
            http2Connector.connect();
        } catch (Exception e) {
            log.error("Http2 Connect Exception. {} ", e);
        }
    }

    public void close(){
        try {
            http2Connector.close();
        } catch (IOException e) {
            log.error("Http2 Close IOException. {} ", e);
        }
    }


    /**
     *
     * @param deviceToken
     * @param payload
     * @return
     * @throws AuthenticationException
     */
    public ChannelFuture send(String deviceToken, Payload payload) throws AuthenticationException {

        if(getAccessToken() == null){
            throw new AuthenticationException("Not Found Apns Access Token. Make CreateToken()");
        }

        if(!validate(getAccessToken())){
            throw new AuthenticationException("Apns Access Token invalidate. Make RefeshToken()");
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(ApnsHeader.AUTHORIZATION, "Bearer " + getAccessToken());
        headerMap.put(ApnsHeader.ID, UUID.randomUUID().toString());

        log.info("Send devicetoken : {}", deviceToken);
        return http2Connector.send(deviceToken, payload, headerMap);
    }

    /**
     *
      * @param deviceToken
     * @param payload
     * @param bundleID {APP ID}
     * @return
     * @throws AuthenticationException
     */
    public ChannelFuture send(String deviceToken, Payload payload, String bundleID) throws AuthenticationException {

        if(getAccessToken() == null){
            throw new AuthenticationException("Not Found Apns Access Token. Make CreateToken()");
        }

        if(!validate(getAccessToken())){
            throw new AuthenticationException("Apns Access Token invalidate. Make RefeshToken()");
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(ApnsHeader.AUTHORIZATION, "Bearer " + getAccessToken());
        headerMap.put(ApnsHeader.ID, UUID.randomUUID().toString());
        headerMap.put(ApnsHeader.TOPIC,bundleID);

        log.info("Send devicetoken : {}", deviceToken);

        return http2Connector.send(deviceToken, payload, headerMap);
    }
    /**
     *
     * @param deviceToken
     * @param payload
     * @param bundleID {APP ID}
     * @param priority
     * @return
     * @throws AuthenticationException
     */
    public ChannelFuture send(String deviceToken, Payload payload, String bundleID, int priority) throws AuthenticationException {

        if(getAccessToken() == null){
            throw new AuthenticationException("Not Found Apns Access Token. Make CreateToken()");
        }
        if(!validate(getAccessToken())){
            throw new AuthenticationException("Apns Access Token invalidate. Make RefeshToken()");
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(ApnsHeader.AUTHORIZATION, "Bearer " + getAccessToken());
        headerMap.put(ApnsHeader.ID, UUID.randomUUID().toString());
        headerMap.put(ApnsHeader.TOPIC, bundleID);
        headerMap.put(ApnsHeader.PRIORITY, priority);


        log.info("Send devicetoken : {}", deviceToken);

        return http2Connector.send(deviceToken, payload, headerMap);
    }

    /**
     *
     * @param deviceToken
     * @param payload
     * @param bundleID
     * @param priority
     * @param expire {Unix epoch date expressed in seconds.}
     * @return
     * @throws AuthenticationException
     */
    public ChannelFuture send(String deviceToken, Payload payload, String bundleID, int priority, int expire) throws AuthenticationException {

        if(getAccessToken() == null){
            throw new AuthenticationException("Not Found Apns Access Token. Make CreateToken()");
        }

        if(!validate(getAccessToken())){
            throw new AuthenticationException("Apns Access Token invalidate. Make RefeshToken()");
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(ApnsHeader.AUTHORIZATION, "Bearer " + getAccessToken());
        headerMap.put(ApnsHeader.ID, UUID.randomUUID().toString());
        headerMap.put(ApnsHeader.TOPIC, bundleID);
        headerMap.put(ApnsHeader.PRIORITY, priority);
        headerMap.put(ApnsHeader.EXPIRATION, expire);

        return http2Connector.send(deviceToken, payload, headerMap);
    }

    /**
     *
     * @param deviceToken
     * @param payload
     * @param bundleID
     * @param priority
     * @param expire
     * @param collapse
     * @return
     * @throws AuthenticationException
     */
    public ChannelFuture send(String deviceToken, Payload payload, String bundleID, int priority, int expire, String collapse) throws AuthenticationException {

        if(getAccessToken() == null){
            throw new AuthenticationException("Not Found Apns Access Token. Make CreateToken()");
        }

        if(!validate(getAccessToken())){
            throw new AuthenticationException("Apns Access Token invalidate. Make RefeshToken()");
        }

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(ApnsHeader.AUTHORIZATION, "Bearer " + getAccessToken());
        headerMap.put(ApnsHeader.ID, UUID.randomUUID().toString());
        headerMap.put(ApnsHeader.TOPIC, bundleID);
        headerMap.put(ApnsHeader.PRIORITY, priority);
        headerMap.put(ApnsHeader.EXPIRATION, expire);
        headerMap.put(ApnsHeader.COLLAPSE, collapse);

        return http2Connector.send(deviceToken, payload, headerMap);
    }


    public String getAccessToken() {
        return accessToken;
    }


    public Http2ResponseListener getHttp2ResponseListener() {
        return http2ResponseListener;
    }

    public void setHttp2ResponseListener(Http2ResponseListener http2ResponseListener) {
        this.http2ResponseListener = http2ResponseListener;
        http2Connector.setHttp2ResponseListener(http2ResponseListener);
    }
}
