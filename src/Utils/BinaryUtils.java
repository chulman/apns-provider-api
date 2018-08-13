package Utils;

public class BinaryUtils {
	
	public static final String SANDBOX_HOST = "gateway.sandbox.push.apple.com";
	public static final String PRODUCTION_HOST = "gateway.sandbox.push.apple.com";

	public static final int PORT = 2195;
	
	public static final String FEEDBACK_HOST = "feedback.sandbox.push.apple.com";
	
	public static final int FEEDBACK_PORT = 2196;
	
	
	//Binary Command
	public static final int COMMAND_NOTIFICATION = 2;

	//Frame Command
	public static final short ITEM_ID_DEVICE_TOKEN = 1;
	public static final short ITEM_ID_PAYLOAD = 2;
	public static final short ITEM_ID_NOTIFICATION_ID = 3;
	public static final short ITEM_ID_EXPIRATION_DATE = 4;
	public static final short ITEM_ID_PRIORITY = 5;

	public static final int MAX_PAYLOAD_BYTES = 2048;
	
	public static final int PRIORITY_BYTE_LENGTH = 1;
	public static final int NOTIFICATION_ID_BYTE_LENGTH = 4;
	public static final int EXPIRATION_BYTE_LENGTH = 4;
	
	public static final String defaultCharSet = "UTF-8";
	

}
