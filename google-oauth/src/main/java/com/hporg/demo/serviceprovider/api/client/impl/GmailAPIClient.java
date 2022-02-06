package com.hporg.demo.serviceprovider.api.client.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author hrishabh.purohit
 */
public class GmailAPIClient implements IServiceProviderAPIClient<Gmail>{
    
    private static final Logger LOGGER = LogManager.getLogger(GmailAPIClient.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APP_NAME_FOR_GOOGLE_OAUTH = "GoogleOAuthDemo";
    private String user;

    @Override
    public String resolveUserActionToClientActionAndExecute(String userAction, Gmail apiClientService) {
        String responseFromClientAction = null;

        switch (userAction) {
            case "READ_MAIL":
                try {
                    responseFromClientAction = readMail(apiClientService);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "READ_LABEL":
                try {
                    responseFromClientAction = readLabel(apiClientService);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        return responseFromClientAction;
    }

    private String readMail(Gmail apiClientService) throws IOException{
        String clientResponse = null;

        ListMessagesResponse messageResponse = apiClientService
                                                .users()
                                                .messages()
                                                .list(this.user)
                                                .execute();
        Message message = messageResponse.getMessages().get(0);
        clientResponse = message.toPrettyString();
        
        return clientResponse;
    }

    private String readLabel(Gmail apiClientService) throws IOException{
        String clientResponse = null;

        ListLabelsResponse labelResponse = apiClientService
                                            .users()
                                            .labels()
                                            .list(this.user)
                                            .execute();
        
        List<Label> labels = labelResponse.getLabels();
        StringBuilder stringBuilder = new StringBuilder("");

        for(Label label : labels){
            stringBuilder.append(label.getName());
        }
        clientResponse = stringBuilder.toString();
        
        return clientResponse;
    }

    @Override
    public void setUserForOperation(String user) {
        this.user = user;
    }

    @Override
    public Gmail buildAPIClientServiceObject(AbstractOAuthToken oauthCredential) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, (Credential)oauthCredential.getOAuthCredentialObject())
                        .setApplicationName(APP_NAME_FOR_GOOGLE_OAUTH)
                        .build();
    }
}
