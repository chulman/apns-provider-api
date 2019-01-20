package com.chulm.apns.format.apns;

import com.chulm.apns.format.constants.ApnsHttpsCode;

public class HttpResponse {

    private String apns_id;
    private ApnsHttpsCode apnsHttpsCode;

    private String error;
    private String timeStamp;


    public HttpResponse() {}

    public HttpResponse(String apns_id, ApnsHttpsCode apnsHttpsCode, String error, String timeStamp) {
        this.apns_id = apns_id;
        this.apnsHttpsCode = apnsHttpsCode;
        this.error = error;
        this.timeStamp = timeStamp;
    }

    public String getApns_id() {
        return apns_id;
    }

    public void setApns_id(String apns_id) {
        this.apns_id = apns_id;
    }

    public ApnsHttpsCode getApnsHttpsCode() {
        return apnsHttpsCode;
    }

    public void setApnsHttpsCode(ApnsHttpsCode apnsHttpsCode) {
        this.apnsHttpsCode = apnsHttpsCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "apns_id='" + apns_id + '\'' +
                ", apnsHttpsCode=" + apnsHttpsCode +
                ", error='" + error + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
