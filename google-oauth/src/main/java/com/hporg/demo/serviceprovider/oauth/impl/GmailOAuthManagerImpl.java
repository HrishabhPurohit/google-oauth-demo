package com.hporg.demo.serviceprovider.oauth.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager;
import com.hporg.demo.utils.GoogleOAuthDemoUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 */
@Component
@Qualifier("serviceProviderOAuthManager")
public class GmailOAuthManagerImpl extends AbstractServiceProviderOAuthManager {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final static Logger LOGGER = LogManager.getLogger(GmailOAuthManagerImpl.class);
    private final static String APP_NAME_FOR_GOOGLE_OAUTH = "GoogleOAuthDemo";

    @Autowired
    public GmailOAuthManagerImpl(@Value("${google.oauth.approach}") String approach) {
        super(approach);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Gmail getOAuthCredentials() throws IOException {
        Gmail gmailService = null;

        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            if (this.getApproach().equals("END_CLIENT_INTERACTIVE")) {
                GoogleClientSecrets clientSecret = GoogleClientSecrets.load(JSON_FACTORY,
                        new InputStreamReader(GoogleOAuthDemoUtil.getCredentialFileForOAuthClient()));

                GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                        .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecret, this.getScopes())
                        .setAccessType("offline")
                        .build();
                
                LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
                Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(this.getUser());

                gmailService = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                                .setApplicationName(APP_NAME_FOR_GOOGLE_OAUTH)
                                .build();
            } else if (this.getApproach().equals("END_CLIENT_NOT_INTERACTIVE")) {
                // TODO: Implement service account approach here.
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return gmailService;
    }

    private class OAuthTokenImpl extends AbstractServiceProviderOAuthManager.AbstractOAuthToken {

        public OAuthTokenImpl(String accessToken, Long expirationTimeInMillis) {
            super(accessToken, expirationTimeInMillis);
        }

        @Override
        public boolean isExpired() {
            Long currentTimeInMillis = new Date().toInstant().toEpochMilli();

            if (this.getExpirationTimeInMillis() <= currentTimeInMillis) {
                return true;
            }

            return false;
        }
    }
}
