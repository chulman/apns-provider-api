package com.chulm.apns.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ConverterUtils {

	public static final byte[] intTo4ByteArray(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public static final byte[] intTo2ByteArray(int value) {
		int s1 = (value & 0xFF00) >> 8;
		int s2 = value & 0xFF;
		return new byte[] { (byte) s1, (byte) s2 };
	}
	
	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < b.length; i++)
			value = (value << 8) | b[i];
		return value;
	}

	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	public static byte[] fromHexaString(String token) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int offset = 0;
		while (offset < token.length()) {
			String str = token.substring(offset, offset + 2);

			int n = Integer.parseInt(str, 16);
			baos.write(n);

			offset += 2;
		}
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static String toHexaString(byte[] token) {
		String tokenString = "";

		for (int i = 0; i < 32; i++) {
			int octet = (0x000000FF & ((int) token[i]));
			tokenString = tokenString.concat(String.format("%02x", octet));
		}
		return tokenString;
	}
}
