package Provider.BinaryAPI.Async;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.SslHandler;

public class SslSetting  {

	private KeyStore ks;
	private KeyManagerFactory kmf;
	private TrustManagerFactory tmf;


	private String algorithem;
	private SslHandler sslHandler;
	private SSLEngine sslEngine;
	

	public SslSetting(String certPath, String certName, String password) {
		
		try {
			ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream(certPath+certName), password.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
			kmf.init(ks, password.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
			tmf.init((KeyStore) null);

			// TLS or SSL protocol get
			SSLContext sc = SSLContext.getInstance("TLS");
			
			sc.init(kmf.getKeyManagers(), null, null);
			
			sslEngine=sc.createSSLEngine();
			sslEngine.setUseClientMode(true);
			
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public KeyStore getKs() {
		return ks;
	}

	public void setKs(KeyStore ks) {
		this.ks = ks;
	}

	public KeyManagerFactory getKmf() {
		return kmf;
	}

	public void setKmf(KeyManagerFactory kmf) {
		this.kmf = kmf;
	}

	public TrustManagerFactory getTmf() {
		return tmf;
	}

	public void setTmf(TrustManagerFactory tmf) {
		this.tmf = tmf;
	}

	public String getAlgorithem() {
		return algorithem;
	}

	public void setAlgorithem(String algorithem) {
		this.algorithem = algorithem;
	}

	public SslHandler getSslHandler() {
		return sslHandler;
	}

	public void setSslHandler(SslHandler sslHandler) {
		this.sslHandler = sslHandler;
	}

	public SSLEngine getSslEngine() {
		return sslEngine;
	}

	public void setSslEngine(SSLEngine sslEngine) {
		this.sslEngine = sslEngine;
	}

}
