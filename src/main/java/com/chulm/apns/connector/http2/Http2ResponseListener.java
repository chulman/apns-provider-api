package com.chulm.apns.connector.http2;

import com.chulm.apns.format.apns.HttpResponse;

public interface Http2ResponseListener {

    public void onMessage(HttpResponse httpResponse);
}
