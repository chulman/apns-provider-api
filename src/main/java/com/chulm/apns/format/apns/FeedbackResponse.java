package com.chulm.apns.format.apns;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Arrays;

public class FeedbackResponse {

    /*
     * A timestamp (as a four-byte time_t value) indicating when APNs determined that the app no longer exists on the device.
     * This value, which is in network order, represents the seconds since 12:00 midnight on January 1, 1970 UTC.
     */
    private Timestamp	timestamp;
    private String		deviceToken;

    public FeedbackResponse(byte[] feedbackTuple) {
        byte[] time_t = Arrays.copyOfRange(feedbackTuple, 0, 4);
        byte[] tokenLength = Arrays.copyOfRange(feedbackTuple, 4, 6);
        byte[] devicetoken = Arrays.copyOfRange(feedbackTuple, 6, 38);

        long time = ByteBuffer.wrap(time_t).getInt();

        setTimestamp(new Timestamp(time * 1000));

            String device = "";
            for (int i = 0; i < 32; i++) {
                int octet = (0x000000FF & ((int) devicetoken[i]));
                device = device.concat(String.format("%02x", octet));
            }

            setDeviceToken(device);
    }

    @Override
    public String toString() {
        return "FeedbackData [timestamp=" + timestamp + ", device token=" + deviceToken + "]";
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
