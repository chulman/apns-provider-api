package main;

import javax.security.sasl.AuthenticationException;

import Provider.Http2TokenProvider;

public class Main_NettyHttp2TokenProvider {
	public static void main(String[] args) {

		String message = "HI";

		/** input your device Token */
		String deviceToken = "";

		/** input your bundle ID */
		String bundleID= "";
		
		String teamID= "";
		String keyID = "";
		
		/** input your secret from authKey.p8
		 *  remove --------public/private key ------------- */
		String secret = "";
		
		/**  remove --------public/private key ------------- */		
				
		try {
			Http2TokenProvider provider = new Http2TokenProvider();
			provider.createToken(keyID, teamID, secret);
			provider.connect();

			provider.send(deviceToken, bundleID, message);
			
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
