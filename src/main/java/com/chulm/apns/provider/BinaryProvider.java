package com.chulm.apns.provider;

import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.connector.ApnsConnector;
import com.chulm.apns.connector.tcp.BinaryConnector;
import com.chulm.apns.connector.tcp.async.BinaryAsyncConnector;
import com.chulm.apns.utils.Constants;

public class BinaryProvider {


    private String host;
    private int port;

    private SslGenerator sslGenerator;
    private boolean production = false;

    private ApnsConnector sync_connector;
    private ApnsConnector async_connector;

    private boolean sync = true;

    public BinaryProvider(SslGenerator sslGenerator, boolean production) {
        this.sslGenerator = sslGenerator;
        this.production = production;

        if (production) {
            host = Constants.BINARY_PRODUCTION_HOST;
            port = Constants.BINARY_PORT;

        } else {
            host = Constants.BINARY_SANDBOX_HOST;
            port = Constants.BINARY_PORT;
        }
    }

    public BinaryConnector sync() {

        if(sync_connector == null){
            sync_connector = new BinaryConnector();
        }
        if (sslGenerator != null) {
            ((BinaryConnector)sync_connector).setSslGenerator(sslGenerator);
        }
        sync_connector.setHost(host);
        sync_connector.setPort(port);

        return (BinaryConnector)sync_connector;

    }

    public BinaryAsyncConnector async() {

        if(async_connector == null){
            async_connector = new BinaryAsyncConnector();
        }
        if (sslGenerator != null) {
            ((BinaryAsyncConnector)async_connector).setSslGenerator(sslGenerator);
        }
        async_connector.setHost(host);
        async_connector.setPort(port);

        return (BinaryAsyncConnector) async_connector;
    }


    public SslGenerator getSslGenerator() {
        return sslGenerator;
    }

    public void setSslGenerator(SslGenerator sslGenerator) {
        this.sslGenerator = sslGenerator;
    }

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }
}
