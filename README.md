# ApnsProviderAPI
Apple Push Notification Service Provider(Binary, Http2 - certificate or AuthToken)




# Binary Provider API

- Send Notification by Java Socket and Netty Framework
- Connection is TLS Protocol
- If Send Request is Sucess, Response is no receive.
- FeedbackService is only response and close from Apns Server.


ERROR CODE

0  : NoErrorEncountered
1  : Processing Error
2  : Missing Device Token
3  : Missing Topic
4  : Missing Payload
5  : Invalid Token SIze
6  : Invalid Topic Size
7  : Invalid Payload Size
8  : Invalid Token
10 : Shutdown
128: Protocol Error
255: Unknown
