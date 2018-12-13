
package com.culm.apns;

import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.connector.tcp.async.ResponseListener;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.format.apns.Response;
import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.provider.BinaryProvider;

import java.io.IOException;

public class BinaryAsyncProviderTest {

    public static void main(String[] args) {

        String path = System.getProperty("user.dir") + "/lib/";

        SslGenerator sslGenerator = new SslGenerator(path, "apns.p12", "");
        try {
            sslGenerator.generate("sunx509", "PKCS12", "TLS");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        try {

            String deviceToken = "";
            Payload payload = new Payload();
            payload.setTitle("title");
            payload.setBody("body");

            BinaryProvider provider = new BinaryProvider(sslGenerator, false);

            boolean connect = provider.async().connect();
            System.err.println("connect :" + connect);

            provider.async().setResponseListener(new ResponseListener() {
                @Override
                public void onMessage(Response response) {

                    System.err.println(response.toString());
                    try {
                        provider.async().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            provider.async().send( deviceToken, payload, 1);
//            while(true){
//                Thread.sleep(1000);
//                provider.async().send("2b10abb13e86d95284b5485d1ceeea488ad984c9e683e725cd53ac81a6d58226", payload, 0);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

