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
import com.hporg.demo.constants.Constants;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;
import com.hporg.demo.utils.annotation.Retryable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * <p> Client implementation for Gmail API. All the user specific client operations to be included here.
 * <p> All the methods take the client service object {@link Gmail} as an input to perform all the client operations. 
 * Authorization and authentication operations are not to be confused with client specific tasks and shall not be included here.
 */
@Component
@Scope("prototype")
@Qualifier("gmailAPIClient")
public class GmailAPIClient implements IServiceProviderAPIClient<Gmail>{
    
    private static final Logger LOGGER = LogManager.getLogger(GmailAPIClient.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private String user;

    @Override
    public String resolveUserActionToClientActionAndExecute(String userAction, Gmail apiClientService) throws Exception{
        String responseFromClientAction = null;

        switch (userAction) {
            case "READ_MAIL":
                LOGGER.debug("Trying to perform action : " + userAction + " on user : " + this.user);
                responseFromClientAction = readMail(apiClientService);
                break;
            case "READ_LABEL":
                LOGGER.debug("Trying to perform action : " + userAction + " on user : " + this.user);
                responseFromClientAction = readLabel(apiClientService);
                break;
        }

        return responseFromClientAction;
    }

    @Retryable(onException = IOException.class)
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

    @Retryable(onException = IOException.class)
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
    @Retryable(onException = IOException.class)
    public Gmail buildAPIClientServiceObject(AbstractOAuthToken oauthCredential) throws GeneralSecurityException, IOException {
        NetHttpTransport http_transport;
        http_transport = GoogleNetHttpTransport.newTrustedTransport();

        final NetHttpTransport HTTP_TRANSPORT = http_transport;
        
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, (Credential)oauthCredential.getOAuthCredentialObject())
                        .setApplicationName(Constants.APP_NAME_FOR_GOOGLE_OAUTH)
                        .build();
    }
}
