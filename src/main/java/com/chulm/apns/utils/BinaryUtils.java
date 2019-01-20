package com.chulm.apns.utils;

public class BinaryUtils {

	//Binary Command
	public static final byte COMMAND_NOTIFICATION = 0x02;

	//Frame Command
	public static final byte ITEM_ID_DEVICE_TOKEN = 0x01;
	public static final byte ITEM_ID_PAYLOAD = 0x02;
	public static final byte ITEM_ID_NOTIFICATION_ID = 0x03;
	public static final byte ITEM_ID_EXPIRATION_DATE = 0x04;
	public static final byte ITEM_ID_PRIORITY = 0x05;

	public static final int MAX_PAYLOAD_BYTES = 2048;
	
	public static final int PRIORITY_BYTE_LENGTH = 1;
	public static final int NOTIFICATION_ID_BYTE_LENGTH = 4;
	public static final int EXPIRATION_BYTE_LENGTH = 4;
	
	public static final String defaultCharSet = "UTF-8";
	

}
