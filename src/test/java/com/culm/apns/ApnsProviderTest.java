package com.culm.apns;

import com.chulm.apns.connector.http2.Http2ResponseListener;
import com.chulm.apns.exception.AuthenticationException;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.format.apns.HttpResponse;
import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.provider.ApnsProvider;

public class ApnsProviderTest {

    public  static void main(String[] args){

        String keyID= "";
        String teamID = "";

        String secret = "";


        ApnsProvider provider = new ApnsProvider(false);

        try {
            provider.createToken(keyID,teamID,secret);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            return;
        }

        provider.connect();

        provider.setHttp2ResponseListener(new Http2ResponseListener() {
            @Override
            public void onMessage(HttpResponse httpResponse) {
                System.err.println(httpResponse.toString());
            }
        });

        String deviceToken = "";
        String bundle_id = "";

        Payload payload = new Payload();
        payload.setTitle("title");
        payload.setBody("body");

        while(true){
            try {
                Thread.sleep(1000);
                provider.send(deviceToken,payload,bundle_id,10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
        }

//        provider.close();

    }
}
//a