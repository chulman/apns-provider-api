
# APNS(Apple Push Notification Service) API
Apple Push Notification Service Provider.


## required
- Netty Framework
- You can only use this API in JAVA 8 (jdk 1.8 or later)


## Binary Provider 

- Protocol is TCP base
- Send Notification by Java Socket and Netty Framework
- Connection is TLS Protocol
- If Send Request is Sucess, Response is no receive.
- FeedbackService is only response and close from Apns Server.


1 . Sync Connector

```
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
            Response response = provider.sync().send("token", payload);

            provider.sync().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
```

2 . Async Connector

```
  SslGenerator sslGenerator = new SslGenerator(path, "apns.p12", "password");
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
```

## Apns Provider

- Protocol is http2 Base.
- this provider is only async.
- Maked with netty Framework Http2 Codec
- There are two kinds of connection authentication.
    + TLS 1.2 Protocol with Apns Certificate.
    + TLS 1.2 Protocol with Apns JSON Web Token.



1 . JWT Connector

```
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
        int priority = "";

        Payload payload = new Payload();
        payload.setTitle("title");
        payload.setBody("body");
          
        provider.send(deviceToken,payload,bundle_id,priority);
        
```


## Feedback Service

- if IOS device token is changed, the binary provider is processed successfully.
- so, feeback service is received modified token from 2196 port.
- send request and open feedback service port.

```
  SslGenerator sslGenerator = new SslGenerator(path, "apns.p12", "password");
        
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
            Response response = provider.sync().send("token", payload);

            provider.sync().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    

        FeedbackConnector feedbackConnector = new FeedbackConnector();
        feedbackConnector.setSslGenerator(sslGenerator);

        List<FeedbackResponse> feedsLists= feedbackConnector.receiveData(Constants.BINARY_SANDBOX_HOST, Constants.FEEDBACK_PORT);

        for(int i =0 ;i <feedsLists.size() ; i++){
            System.err.println(feedsLists.get(i).toString());
        }
```