package com.chulm.apns.format.constants;


public enum StatusCode {
	/**
	 * 발생한 에러 없음. 전송 성공
	 */
	NoErrorsEncountered(0),
	/**
	 * APNS 내부 처리 에러
	 */
	ProcessingError(1),
	/**
	 * APNS로 보낸 메시지에 Device Token이 없음
	 */
	MissingDeviceToken(2),
	/**
	 * APNS로 보낸 메시지에 Topic이 없음
	 */
	MissingTopic(3),
	/**
	 * APNS로 보낸 메시지에 Payload가 없음
	 */
	MissingPayload(4),
	/**
	 * Device Token의 크기가 32byte가 아님
	 */
	InvalidTokenSize(5),
	/**
	 * Topic 크기 오류
	 */
	InvalidTopicSize(6),
	/** 
	 * Payload 크기가 256byte를 초과
	 */
	InvalidPayloadSize(7),
	/**
	 * Device Token이 유효하지 않음
	 */
	InvalidToken(8),
	/**
	 * APNS 서버가 Shutdown 됨
	 */
	Shutdown(10),
	/**
	 * 알수 없는 원인
	 */
	Unknown(255);
	
	private int	value;

	private StatusCode(int value) {
		this.value = value;
	}
	
	public static StatusCode valueOf(byte statuscode) {
		switch (statuscode) {
			case 0x00: return NoErrorsEncountered;
			case 0x01: return ProcessingError;
			case 0x02: return MissingDeviceToken;
			case 0x03: return MissingTopic;
			case 0x04: return MissingPayload;
			case 0x05: return InvalidTokenSize;
			case 0x06: return InvalidTopicSize;
			case 0x07: return InvalidPayloadSize;
			case 0x08: return InvalidToken;
			case 0x0A: return Shutdown;
			case (byte) 0xFF: return Unknown;
		}
		return null;
	}
	
	public static StatusCode valueOf(int statuscode) {
		switch (statuscode) {
			case   0: return NoErrorsEncountered;
			case   1: return ProcessingError;
			case   2: return MissingDeviceToken;
			case   3: return MissingTopic;
			case   4: return MissingPayload;
			case   5: return InvalidTokenSize;
			case   6: return InvalidTopicSize;
			case   7: return InvalidPayloadSize;
			case   8: return InvalidToken;
			case  10: return Shutdown;
			case 255: return Unknown;
		}
		return null;
	}


	public int intValue() {
		return value;
	}

	public byte[] getBytes() {
		return this.toString().getBytes();
	}

	public char[] toCharArrays() {
		return this.toString().toCharArray();
	}
}