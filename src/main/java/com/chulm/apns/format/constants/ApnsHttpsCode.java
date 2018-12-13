package com.chulm.apns.format.constants;

public enum ApnsHttpsCode {

    Success(200),

    BadReqest(400),

    UnAuthorized(403),

    InvalidMethod(405),

    InActiveDeviceToken(410),

    PayLoadTooLarge(413),

    TooManyRequestSameDevice(429),

    InternalServerError(500),

    Unavailable(503);

    private int	value;

    private ApnsHttpsCode(int value) {
        this.value = value;
    }

    public static ApnsHttpsCode valueOf(int headerCode) {
        switch (headerCode) {
            case   200: return Success;
            case   400: return BadReqest;
            case   403: return UnAuthorized;
            case   405: return InvalidMethod;
            case   410: return InActiveDeviceToken;
            case   413: return PayLoadTooLarge;
            case   429: return TooManyRequestSameDevice;
            case   500: return InternalServerError;
            case   503: return Unavailable;
        }
        return null;
    }
}
