package main;

import java.io.IOException;

import Provider.AsyncBinaryProvider;

public class Main_AsyncBinaryProvider {
	public static void main(String[] args) {

		String certPath = System.getProperty("user.dir") + "\\lib\\";
		String certName = "swift-certificate.p12";
		
		/** input your Certificate Password */
		String password = "";

		System.out.println(certPath);
		
		String message = "HI";
		
		/** input your device Token */
		String deviceToken = "";
		
		AsyncBinaryProvider provider = new AsyncBinaryProvider();

		try {
			provider.setConfig(certPath, certName, password);
			provider.connect();
			provider.send(message, deviceToken);

		/**
		 *  NettyFrameWork is Asynchronous Event Driven Framework. 
		 *  So if Close is faster than Send to Apns Server, Notification is not Send to Device */
//			provider.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
