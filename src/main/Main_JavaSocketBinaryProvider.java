package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import Provider.BinaryAPI.BinaryProvider;
import Provider.BinaryAPI.FeedbackService.Feedback;

public class Main_JavaSocketBinaryProvider {
	public static void main(String[] args) {

		String certPath = System.getProperty("user.dir") + "\\lib\\";
		String certName = "swift-certificate.p12";
		
		/** input your Certificate Password */
		String password = "tobe0701";
		
		System.out.println(certPath);
		
		String message = "TEST";
		
		/** input your device Token */
		String deviceToken = "";
		
		BinaryProvider provider = new BinaryProvider();
		Feedback feedback = new Feedback();
		
		try {
			provider.setConfig(certPath, certName, password);
			provider.connect();
			provider.send(message, deviceToken);
			String response  = provider.receive();
			System.err.println(response);

			feedback.receiveData(certPath, certName, password);
			
			provider.close();
			
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
