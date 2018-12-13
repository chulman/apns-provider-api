package com.chulm.apns.connector.tcp.async;

import com.chulm.apns.format.apns.Response;

public interface ResponseListener {

    public void onMessage(Response response);
}
