package Provider.BinaryAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import Utils.BinaryUtils;
import Utils.ConverterUtils;

public class Frame {
	private String deviceToken;
	private String payload;
	private int expireDate;
	private int priority;
	private int notificationID;
	
	private int length;
	private byte[] data;

	public Frame(String deviceToken, String payload, int expireDate, int priority, int notificationID) {
		super();
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
		baos.write(ConverterUtils.intTo2ByteArray(payload.getBytes().length));
		baos.write(payload.getBytes(BinaryUtils.defaultCharSet));

		baos.write(BinaryUtils.ITEM_ID_NOTIFICATION_ID);
		baos.write(ConverterUtils.intTo2ByteArray(BinaryUtils.NOTIFICATION_ID_BYTE_LENGTH));
		baos.write(ConverterUtils.intTo4ByteArray(notificationID));

		baos.write(BinaryUtils.ITEM_ID_EXPIRATION_DATE);
		baos.write(ConverterUtils.intTo2ByteArray(BinaryUtils.EXPIRATION_BYTE_LENGTH));
		baos.write(ConverterUtils.intTo4ByteArray(expireDate));

		baos.write(BinaryUtils.ITEM_ID_PRIORITY);
		baos.write(ConverterUtils.intTo2ByteArray(BinaryUtils.PRIORITY_BYTE_LENGTH));
		baos.write((byte)priority);

		length = baos.size();
		data = new byte[baos.size()];
		data = baos.toByteArray();

	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
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
