# ApnsProviderAPI
Apple Push Notification Service Provider(Binary, Http2 - certificate or AuthToken)




# Binary Provider API

- Protocol is TCP Base
- Send Notification by Java Socket and Netty Framework
- Connection is TLS Protocol
- If Send Request is Sucess, Response is no receive.
- FeedbackService is only response and close from Apns Server.



# Http2 Provider API

- Protocol is Http2 Base
- Maked with netty Framework Http2 Codec
- There are two kinds of connection authentication.

1) TLS 1.2 Protocol with Apns Certificate.
2) TLS 1.2 Protocol with Apns JSON Web Token.

- You can only use this API in JAVA 8 (jdk 1.8 or later).
