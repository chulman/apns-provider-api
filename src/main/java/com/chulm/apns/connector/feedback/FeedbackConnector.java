package com.chulm.apns.connector.feedback;

import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.format.apns.FeedbackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedbackConnector {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

	private SSLSocket feedSock;
	private BufferedInputStream bis;

	private SslGenerator sslGenerator;

	public List<FeedbackResponse> receiveData(String host, int port) {

        List<FeedbackResponse> feedbackResponses = new ArrayList<>();

	    SSLSocketFactory ssf  = sslGenerator.getSslSocketFactory();
        try {
            feedSock = (SSLSocket) ssf.createSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            return feedbackResponses;
        }
        String[] cipherSuites = feedSock.getSupportedCipherSuites();
		feedSock.setEnabledCipherSuites(cipherSuites);

        try {
            feedSock.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
            return feedbackResponses;
        }

        log.info("feedback Service Connect {} :" + feedSock.isConnected());

		int read = 0;
		try{
            bis = new BufferedInputStream(feedSock.getInputStream());
            while (true) {
                byte[] tuple = new byte[38];
                read = bis.read(tuple, 0, tuple.length);

                byte[] time_t = Arrays.copyOfRange(tuple, 0, 4);
                byte[] token_length = Arrays.copyOfRange(tuple, 4, 6);
                byte[] token = Arrays.copyOfRange(tuple, 6, tuple.length);

                if (read > 0) {
                    FeedbackResponse feedbackResponse = new FeedbackResponse(tuple);
                    feedbackResponses.add(feedbackResponse);
                } else {
                    break;
                }
            }
        }catch (IOException e){
            return  feedbackResponses;
        }

        try {
            bis.close();
            feedSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

		return feedbackResponses;
	}

    public void setSslGenerator(SslGenerator sslGenerator) {
        this.sslGenerator = sslGenerator;
    }
}
