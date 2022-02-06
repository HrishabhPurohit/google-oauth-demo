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

    @Autowired
    public GmailOAuthManagerImpl(@Value("${google.oauth.approach}") String approach) {
        super(approach);
    }

    @Override
    public OAuthTokenImpl getOAuthCredentials() throws IOException {
        OAuthTokenImpl oauthCredentialObject = null;

        try {
            if(GoogleOAuthDemoUtil.isTokenCached(this.getUser())){
                oauthCredentialObject = (OAuthTokenImpl) GoogleOAuthDemoUtil.getTokenFromTokenCache(this.getUser());
                if(oauthCredentialObject.isExpired()){
                    oauthCredentialObject.refreshToken();
                }
                
                return oauthCredentialObject;
            }

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
                credential.refreshToken();

                oauthCredentialObject = new OAuthTokenImpl(credential);
                cacheUserOAuthToken(oauthCredentialObject);
            } else if (this.getApproach().equals("END_CLIENT_NOT_INTERACTIVE")) {
                // TODO: Implement service account approach here.
                /**Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(this.getUser());
                credential.refreshToken();

                oauthCredentialObject = new OAuthTokenImpl(credential);
                cacheUserOAuthToken(oauthCredentialObject); */
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return oauthCredentialObject;
    }

    private void cacheUserOAuthToken(OAuthTokenImpl oauthToken){
        GoogleOAuthDemoUtil.initTokenCaching(this.getUser(), oauthToken);
    }

    private class OAuthTokenImpl extends AbstractServiceProviderOAuthManager.AbstractOAuthToken {

        private Credential oauthCredentialObject;

        public OAuthTokenImpl(Credential oauthCredentialObject) {
            this.oauthCredentialObject = oauthCredentialObject;
            this.setAccessToken(oauthCredentialObject.getAccessToken());
            this.setExpirationTimeInMillis(oauthCredentialObject.getExpirationTimeMilliseconds());
        }

        @Override
        public boolean isExpired() {
            Long currentTimeInMillis = new Date().toInstant().toEpochMilli();

            if (this.getExpirationTimeInMillis() <= currentTimeInMillis) {
                return true;
            }

            return false;
        }

        @Override
        public void refreshToken() throws IOException {
            this.oauthCredentialObject.refreshToken();
            this.setAccessToken(oauthCredentialObject.getAccessToken());
            this.setExpirationTimeInMillis(oauthCredentialObject.getExpirationTimeMilliseconds());
        }

        @Override
        @SuppressWarnings("unchecked")
        public Credential getOAuthCredentialObject() {
            return this.oauthCredentialObject;
        }
    }
}
