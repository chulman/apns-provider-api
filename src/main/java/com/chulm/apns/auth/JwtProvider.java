package com.chulm.apns.auth;

/*
 * getInstance가 호출되고 lazyHodler.Instance를 참조하는 순간 Class가 로딩되고 초기화가 진행된다.
 * 클래스가 로딩하고 초기화하는 시점은 thread-safe를 보장한다.
 */

import com.chulm.apns.exception.TokenVerifyException;
import com.chulm.apns.format.jwt.JwtHeader;
import com.chulm.apns.format.jwt.JwtPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author chulm
 */
public class JwtProvider {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    enum Singleton {
        INSTANCE;
    }

    private JwtProvider() {
    }

    public static JwtProvider getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final JwtProvider INSTANCE = new JwtProvider();
    }


    /**
     *
     * Parameter is All Registered Claim
     *
     * @param keyID
     * @param teamID
     * @param secret {String Data in AuthKey.p8 from Apns Development Site}
     * @return
     * @throws Exception
     */
    public String createToken(String keyID, String teamID, String secret) throws Exception {

        long nowMillis = System.currentTimeMillis()/1000;

        String header = objectMapper.writeValueAsString(new JwtHeader("ES256",keyID));
        String payload = objectMapper.writeValueAsString(new JwtPayload(teamID, nowMillis));

        String encoded = encoding(header,payload);
        String token = signature(encoded, secret);


        log.info("Create JWT Token. {}", token);
        return token;

    }


    public boolean validate(String token) throws TokenVerifyException {

        boolean verify = true;
        String[] tokenArr = token.split("\\.");
       JwtHeader jwtHeader =  decoding_header(tokenArr[0]);

       if(!jwtHeader.getAlg().equals("ES256")){
           verify = false;
       }
       JwtPayload jwtPayload = decding_payload(tokenArr[1]);

       long cuurentTime = System.currentTimeMillis() / 1000;
       long createTime = jwtPayload.getIat();

       long day = 86400000;

       if((cuurentTime - createTime) > day){
           verify = false;
       }

       return verify;
    }




    private String encoding(String header, String payload){

        String base64Header = new String(Base64.getEncoder().encode(header.getBytes(StandardCharsets.UTF_8)));
        String base64Payload = new String(Base64.getEncoder().encode(payload.getBytes(StandardCharsets.UTF_8)));

        StringBuffer buffer = new StringBuffer();
        buffer.append(base64Header);
        buffer.append(".");
        buffer.append(base64Payload);

        return buffer.toString();
    }

    private JwtHeader decoding_header(String base64Header) throws TokenVerifyException {
        byte[]  bytes = Base64.getDecoder().decode(base64Header.getBytes(StandardCharsets.UTF_8));
        String header = new String(bytes,StandardCharsets.UTF_8);
        JwtHeader jwtHeader = null;
        try {
            jwtHeader = objectMapper.readValue(header,JwtHeader.class);
        } catch (IOException e) {
            throw new TokenVerifyException("Token is not valid.");
        }
        return jwtHeader;
    }

    private JwtPayload decding_payload(String base64payload) throws TokenVerifyException {
        byte[]  bytes = Base64.getDecoder().decode(base64payload.getBytes(StandardCharsets.UTF_8));
        String header = new String(bytes,StandardCharsets.UTF_8);

        JwtPayload jwtPayload = null;
        try {
            jwtPayload = objectMapper.readValue(header,JwtPayload.class);
        } catch (IOException e) {
            throw new TokenVerifyException("Token is not valid.");
        }
        return jwtPayload;
    }

    private String signature(String encoded, String secret){

        StringBuffer buffer = new StringBuffer();

        buffer.append(encoded);
        buffer.append(".");
        buffer.append(ES256(secret,encoded));

        return buffer.toString();
    }


    private String ES256(String secret, String data)  {

        KeyFactory kf = null;
        PrivateKey key = null;
        Signature sha256withECDSA = null;
        byte[] signed= null;

        try {
            kf = KeyFactory.getInstance("EC");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(secret));

            key = kf.generatePrivate(keySpec);

            sha256withECDSA = Signature.getInstance("SHA256withECDSA");
            sha256withECDSA.initSign(key);

            sha256withECDSA.update(data.getBytes(CharsetUtil.UTF_8));
            signed = sha256withECDSA.sign();

        } catch (NoSuchAlgorithmException e) {
            log.error("JWT Signature Fail. {}" + e);
            throw new TokenVerifyException();
        } catch (SignatureException e) {
            log.error("JWT Signature Fail. {}" + e);
            throw new TokenVerifyException();
        } catch (InvalidKeyException e) {
            log.error("JWT Signature Fail. {}" + e);
            throw new TokenVerifyException();
        } catch (InvalidKeySpecException e) {
            log.error("JWT Signature Fail. {}" + e);
            throw new TokenVerifyException();
        }finally {
            if(signed == null){
                return null;
            }

            return new String(Base64.getEncoder().encode(signed),CharsetUtil.UTF_8);
        }
    }



}
