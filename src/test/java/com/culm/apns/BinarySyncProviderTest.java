package com.culm.apns;

import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.format.apns.Response;
import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.provider.BinaryProvider;

public class BinarySyncProviderTest {

    public static void main(String[] args) {

        String path = System.getProperty("user.dir") + "/lib/";

        SslGenerator sslGenerator = new SslGenerator(path, "apns.p12", "");
        try {
            sslGenerator.generate("sunx509", "PKCS12", "TLS");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        BinaryProvider provider = new BinaryProvider(sslGenerator,false);


        try {
            Payload payload = new Payload();
            payload.setTitle("title");
            payload.setBody("body");

            provider.sync().connect();
            Response response = provider.sync().send("", payload);
            System.err.println("----->" + response);

            provider.sync().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
