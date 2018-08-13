package Provider.BinaryAPI.FeedbackService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
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

import Utils.BinaryUtils;
import Utils.ConverterUtils;

public class Feedback {

	private SSLSocket feedSock;
	private BufferedInputStream bis;

	public void receiveData(String certPath, String cert, String password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException {

		KeyStore ks = null;
		ks = KeyStore.getInstance("PKCS12");
		ks.load(new FileInputStream(certPath + cert), password.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
		kmf.init(ks, password.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
		tmf.init((KeyStore) null);

		SSLContext sc = SSLContext.getInstance("TLS");
		// SSLContext sc = SSLContext.getInstance("SSL");

		sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		SSLSocketFactory ssf = sc.getSocketFactory();

		feedSock = (SSLSocket) ssf.createSocket(BinaryUtils.FEEDBACK_HOST, BinaryUtils.FEEDBACK_PORT);
		String[] cipherSuites = feedSock.getSupportedCipherSuites();
		feedSock.setEnabledCipherSuites(cipherSuites);

		feedSock.startHandshake();

		System.out.println("feedback Service Connect :" + feedSock.isConnected());

		int a = 0;
		bis = new BufferedInputStream(feedSock.getInputStream());
		while (true) {
			byte[] tuple = new byte[38];
			a = bis.read(tuple, 0, tuple.length);
			System.out.println(a);

			byte[] time_t = Arrays.copyOfRange(tuple, 0, 4);
			byte[] token_length = Arrays.copyOfRange(tuple, 4, 6);
			byte[] token = Arrays.copyOfRange(tuple, 6, tuple.length);
			if (a > 0) {
				System.out.println("time_t:" + ConverterUtils.byteArrayToInt(time_t));
				System.out.println("token_length:" + ConverterUtils.byteArrayToInt(token_length));
				System.out.println("token:" + ConverterUtils.byteArrayToHex(token));
			} else {
				break;
			}
		}

		bis.close();
		feedSock.close();
	}
}
