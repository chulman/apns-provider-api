package com.culm.apns;

import com.chulm.apns.auth.SslGenerator;
import com.chulm.apns.connector.feedback.FeedbackConnector;
import com.chulm.apns.exception.ResourceNotFoundException;
import com.chulm.apns.format.apns.FeedbackResponse;
import com.chulm.apns.utils.Constants;

import java.util.List;

public class FeedbackProviderTest {

    public static void main(String[] args){

        String path = System.getProperty("user.dir") + "/lib/";

        SslGenerator sslGenerator = new SslGenerator(path, "apns.p12", "");
        try {
            sslGenerator.generate("sunx509", "PKCS12", "TLS");
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        FeedbackConnector feedbackConnector = new FeedbackConnector();
        feedbackConnector.setSslGenerator(sslGenerator);

        List<FeedbackResponse> feedsLists= feedbackConnector.receiveData(Constants.BINARY_SANDBOX_HOST, Constants.FEEDBACK_PORT);

        for(int i =0 ;i <feedsLists.size() ; i++){
            System.err.println(feedsLists.get(i).toString());
        }

    }
}
