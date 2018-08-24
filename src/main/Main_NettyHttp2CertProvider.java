package main;

import Provider.Http2CertProvider;

public class Main_NettyHttp2CertProvider {
	public static void main(String[] args) {

		String certPath = System.getProperty("user.dir") + "\\lib\\";
		String certName = "swift-certificate.p12";
		
		/** input your Certificate Password */
		String password = "";

		System.out.println(certPath);
		
		String message = "HI";
		
		/** input your device Token */
		String deviceToken = "";
	
		Http2CertProvider provider = new Http2CertProvider();
		provider.setConfig(certPath, certName, password);
		
		provider.connect();
		provider.send(deviceToken, message);
//		provider.close();
	}
}
