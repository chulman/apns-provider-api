package com.chulm.apns.format.jwt;

public class JwtPayload {

    private String iss;
    private long iat;

    public JwtPayload() {
    }

    public JwtPayload(String iss, long iat) {
        this.iss = iss;
        this.iat = iat;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }
}
