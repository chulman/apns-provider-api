package com.chulm.apns.connector;

import com.chulm.apns.auth.SslGenerator;

import java.io.IOException;

public abstract class ApnsConnector {

    protected SslGenerator sslGenerator;

    protected String host;
    protected  int port;

    public abstract boolean connect() throws Exception;

    public abstract void close() throws IOException;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
