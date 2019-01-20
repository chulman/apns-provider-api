package com.chulm.apns.format.apns;

import com.chulm.apns.format.constants.StatusCode;

public class Response {

    private long command;
    private long identifier;
    private StatusCode statusCode;
    private String deviceToken;


    public Response() { }

    public Response(long command, long identifier, StatusCode statusCode, String deviceToken) {
        this.command = command;
        this.identifier = identifier;
        this.statusCode = statusCode;
        this.deviceToken = deviceToken;
    }

    public long getCommand() {
        return command;
    }

    public void setCommand(long command) {
        this.command = command;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "Response{" +
                "command=" + command +
                ", identifier=" + identifier +
                ", statusCode=" + statusCode +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}
