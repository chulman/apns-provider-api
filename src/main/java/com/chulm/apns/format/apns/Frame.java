package com.chulm.apns.format.apns;

import com.chulm.apns.format.payload.Payload;
import com.chulm.apns.utils.BinaryUtils;
import com.chulm.apns.utils.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Frame {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String deviceToken;
    private Payload payload;
    private int expireDate = 0;
    private int priority = 10;
    private int notificationID = 0;

    private int length;
    private byte[] data;

    public Frame() {
    }

    public Frame(String deviceToken, Payload payload) {
        this.deviceToken = deviceToken;
        this.payload = payload;
    }

    public Frame(String deviceToken, Payload payload, int notificationID) {
        this.deviceToken = deviceToken;
        this.payload = payload;
        this.notificationID = notificationID;
    }

    public Frame(String deviceToken, Payload payload, int notificationID, int expireDate) {
        this.deviceToken = deviceToken;
        this.payload = payload;
        this.expireDate = expireDate;
        this.notificationID = notificationID;
    }

    public Frame(String deviceToken, Payload payload, int notificationID, int expireDate, int priority) {
        this.deviceToken = deviceToken;
        this.payload = payload;
        this.expireDate = expireDate;
        this.priority = priority;
        this.notificationID = notificationID;
    }

    public void pack() throws IOException {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(BinaryUtils.ITEM_ID_DEVICE_TOKEN);
        baos.write(ConverterUtils.intTo2ByteArray(32));
        baos.write(DatatypeConverter.parseHexBinary(deviceToken));

        baos.write(BinaryUtils.ITEM_ID_PAYLOAD);
        baos.write(ConverterUtils.intTo2ByteArray(payload.parseToJson().length()));
        baos.write(payload.parseToJson().getBytes(BinaryUtils.defaultCharSet));

        baos.write(BinaryUtils.ITEM_ID_NOTIFICATION_ID);
        baos.write(ConverterUtils.intTo2ByteArray(BinaryUtils.NOTIFICATION_ID_BYTE_LENGTH));
        baos.write(ConverterUtils.intTo4ByteArray(notificationID));

        baos.write(BinaryUtils.ITEM_ID_EXPIRATION_DATE);
        baos.write(ConverterUtils.intTo2ByteArray(BinaryUtils.EXPIRATION_BYTE_LENGTH));
        baos.write(ConverterUtils.intTo4ByteArray(expireDate));

        baos.write(BinaryUtils.ITEM_ID_PRIORITY);
        baos.write(ConverterUtils.intTo2ByteArray(BinaryUtils.PRIORITY_BYTE_LENGTH));
        baos.write((byte) priority);

        length = baos.size();
        data = new byte[baos.size()];
        data = baos.toByteArray();

        baos.close();
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public int getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(int expireDate) {
        this.expireDate = expireDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
