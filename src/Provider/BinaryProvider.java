package Provider;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import Provider.BinaryAPI.Frame;
import Utils.BinaryUtils;
import Utils.ConverterUtils;
import Utils.JsonUtils;


public class BinaryProvider {

	private SSLSocket sSock;
	private OutputStream outputStream;
	
	private String payload;
	
	private int expireDate = 4;
	private int priority = 10;
	private int notificationID = 5;
	
	public void setConfig(String certPath, String certName, String password) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certPath + certName), password.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
		kmf.init(ks, password.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
		tmf.init((KeyStore) null);

		/** TLS or SSL protocol */ 
		SSLContext sc = SSLContext.getInstance("TLS");
//		SSLContext sc = SSLContext.getInstance("SSL");

		sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		SSLSocketFactory ssf = sc.getSocketFactory();
		sSock = (SSLSocket) ssf.createSocket(BinaryUtils.SANDBOX_HOST, BinaryUtils.PORT);
	}
	
	public void connect() throws IOException {

		String[] cipherSuites = sSock.getSupportedCipherSuites();
		sSock.setEnabledCipherSuites(cipherSuites);

		sSock.startHandshake();

		System.out.println("connect:" + sSock.isConnected());
	}
	

	public void send(String message, String deviceToken) throws IOException {
		
		/** message setting */
		payload = JsonUtils.parse(message);
		
		System.err.println(payload.toString());
		
		Frame frame = new Frame(deviceToken, payload, expireDate, priority, notificationID);
		frame.pack();
		
		outputStream = sSock.getOutputStream();
		outputStream.write(BinaryUtils.COMMAND_NOTIFICATION);
		outputStream.write(ConverterUtils.intTo4ByteArray(frame.getLength()));
		outputStream.write(frame.getData());
		outputStream.flush();
	}
	
	public String receive() {
		
		/** response가 없으면 성공이기 때문에 타임아웃 설정*/
	
		
		byte[] resp = new byte[6];
		BufferedInputStream bis = null;

		
		try {
			sSock.setSoTimeout(1500);
			bis = new BufferedInputStream(sSock.getInputStream());
			bis.read(resp, 0, resp.length);
		}catch (SocketTimeoutException e) {
			return "sucess";
		}catch (IOException e) {
			e.printStackTrace();
			
		}finally {
			try {
				bis.close();
				sSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		return Arrays.toString(resp);
	}

	
	public void close() {
		try {
			outputStream.close();
			sSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
