package Provider.Http2API.WebToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import io.netty.util.CharsetUtil;



public class JwtTokenManager {
	private String token;

	
	/** Parameter is All Registered Claim 
	 * 
	 * @param keyID
	 * @param teamID
	 * @param secret = {String Data in AuthKey.p8 from Apns Development Site}
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws SignatureException
	 */
	
	public String createToken(String keyID, String teamID, String secret) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException {
		
		long nowMillis = System.currentTimeMillis()/1000;	
		
		String header = "{\"alg\" : \"ES256\" , \"kid\":\""+ keyID +"\"}";
		String payload = "{\"iss\" : \""+teamID+"\" , \"iat\":" + nowMillis + "}";
		


		String base64Header = new String(Base64.getEncoder().encode(header.getBytes(StandardCharsets.UTF_8)));
		String base64Payload = new String(Base64.getEncoder().encode(payload.getBytes(StandardCharsets.UTF_8)));
		
		String encodingData = base64Header + "." + base64Payload;

		StringBuffer buffer = new StringBuffer();
		buffer.append(encodingData).append(".").append(ES256(secret,encodingData));
		
		token = buffer.toString();
		
		return token;
		
	}
	
	private String ES256( String secret, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

			KeyFactory kf;
	
			kf = KeyFactory.getInstance("EC");
			KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(secret));
			PrivateKey key;
			key = kf.generatePrivate(keySpec);

			final Signature sha256withECDSA = Signature.getInstance("SHA256withECDSA");
			sha256withECDSA.initSign(key);

			sha256withECDSA.update(data.getBytes(CharsetUtil.UTF_8));
			final byte[] signed = sha256withECDSA.sign();

			return new String(Base64.getEncoder().encode(signed),CharsetUtil.UTF_8);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
