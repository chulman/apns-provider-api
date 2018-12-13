package com.chulm.apns.auth;

import com.chulm.apns.exception.ResourceNotFoundException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class SslGenerator {


    private String certPath = null;
    private String certName = null;
    private String certPassword = null;

    private SSLSocketFactory sslSocketFactory;
    private SSLEngine sslEngine;
    private KeyManagerFactory keyManagerFactory;

    public SslGenerator(String certPath, String certName, String certPassword) {
        this.certPath = certPath;
        this.certName = certName;
        this.certPassword = certPassword;

    }

    /**
     * Defalut Createor
     */
    public SSLSocketFactory generate() throws ResourceNotFoundException {

        if (certIsNull()) {
            throw new ResourceNotFoundException("Certificate Resource not found");
        }

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(certPath + certName), certPassword.toCharArray());

            keyManagerFactory = KeyManagerFactory.getInstance("sunx509");
            keyManagerFactory.init(ks, certPassword.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
            tmf.init((KeyStore) null);

            // TLS or SSL protocol get
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(keyManagerFactory.getKeyManagers(), null, null);

            sslEngine = sc.createSSLEngine();
            sslEngine.setUseClientMode(true);

            sc.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
            sslSocketFactory = sc.getSocketFactory();

        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslSocketFactory;
    }

    /**
     *
     * @param algorithm
     * @param KeystoreType
     * @param protocol
     * @return
     */
    public SSLSocketFactory generate(String algorithm, String KeystoreType, String protocol) throws ResourceNotFoundException {

        if (certIsNull()) {
            throw new ResourceNotFoundException("Certificate Resource not found");
        }


        try {
            KeyStore ks = KeyStore.getInstance(KeystoreType);
            ks.load(new FileInputStream(certPath + certName), certPassword.toCharArray());

            keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
            keyManagerFactory.init(ks, certPassword.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init((KeyStore) null);

            // TLS or SSL protocol get
            SSLContext sc = SSLContext.getInstance(protocol);

            sc.init(keyManagerFactory.getKeyManagers(), null, null);

            sslEngine = sc.createSSLEngine();
            sslEngine.setUseClientMode(true);

            sc.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
            sslSocketFactory = sc.getSocketFactory();

        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        return sslSocketFactory;

    }


    private boolean certIsNull() {

        if (certPath == null || certName == null || certPath == null) {
            return true;
        }
        return false;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public SSLEngine getSslEngine() {
        return sslEngine;
    }

    public void setSslEngine(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }
}
