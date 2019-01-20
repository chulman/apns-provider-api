package com.chulm.apns.format.jwt;

public class JwtHeader {

    private String alg  = "ES256";
    private String kid;

    public JwtHeader() {
    }

    public JwtHeader(String alg, String kid) {
        this.alg = alg;
        this.kid = kid;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }
}
