package com.hporg.demo.serviceprovider.oauth.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
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
 * <p><code>AbstractServiceProviderOAuthManager</code> implementation for the <code>Gmail API</code>.
 * <p> Primary responsibilities include:
 * <p> 1. Providing Google Credential object needed for client operations.
 * <p> 2. Taking care of implementation according to the approach provided (either web based or server-to-server).
 * <p> 3. Generating <code>OAuthTokenImpl</code> for a given user.
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
    public OAuthTokenImpl getOAuthCredentials() throws Exception{
        OAuthTokenImpl oauthCredentialObject = null;

        if(GoogleOAuthDemoUtil.isTokenCached(this.getUser())){
            oauthCredentialObject = (OAuthTokenImpl) GoogleOAuthDemoUtil.getTokenFromTokenCache(this.getUser());
            if(oauthCredentialObject.isExpired()){
                try{
                    oauthCredentialObject.refreshToken();
                    return oauthCredentialObject;
                } catch (TokenResponseException tre){
                    LOGGER.error("4xx error occurred while trying to refresh access token for user : " + this.getUser(), tre);
                    LOGGER.error("ERROR: Attempting to rebuild credentials for user : " + this.getUser());
                } catch (Exception e) {
                    throw e;
                }
            }
            else{
                return oauthCredentialObject;
            }
        }

        return getOAuthCredentials(this.getApproach(), GoogleNetHttpTransport.newTrustedTransport());
    }

    private OAuthTokenImpl getOAuthCredentials(String approach, final NetHttpTransport HTTP_TRANSPORT) throws IOException{
        LOGGER.debug("Trying to authenticate user : " + this.getUser() + " with approach : " + this.getApproach());
        OAuthTokenImpl oauthCredentialObject = null;

        if (this.getApproach().equals("END_CLIENT_INTERACTIVE")) {
            GoogleClientSecrets clientSecret = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(GoogleOAuthDemoUtil.getCredentialFileForOAuthClient()));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                    .Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecret, this.getScopes())
                    .setAccessType("offline")
                    .build();
            
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(this.getUser());

            oauthCredentialObject = new OAuthTokenImpl(credential);
        } else if (this.getApproach().equals("END_CLIENT_NOT_INTERACTIVE")) {
            // TODO: Implement service account approach here.
            /**Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(this.getUser());
            credential.refreshToken();

            oauthCredentialObject = new OAuthTokenImpl(credential);
            cacheUserOAuthToken(oauthCredentialObject); */
        }

        return oauthCredentialObject;
    }

    /**
     * @author hrishabh.purohit
     * <p><code>AbstractOAuthToken</code> implementation for <code>Gmail API</code>
     * <p> Primary responsibilities include:
     * <p> 1. Maintaining access token for a given google credential object.
     * <p> 2. Maintaining expiration time for the access token.
     */
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
        public void refreshToken() throws Exception {
            boolean isAccessTokenRefreshed = false;

            try{
               isAccessTokenRefreshed = this.oauthCredentialObject.refreshToken();
            } catch (IOException ioe){
                LOGGER.error("IOException occurred while refreshing token for user : " + getUser() + ". Retrying once again.", ioe);
                isAccessTokenRefreshed = this.oauthCredentialObject.refreshToken();
            }
            
            if(isAccessTokenRefreshed){
                LOGGER.debug("Successfully refreshed access token for user : " + getUser());
                this.setAccessToken(oauthCredentialObject.getAccessToken());
                this.setExpirationTimeInMillis(oauthCredentialObject.getExpirationTimeMilliseconds());
            }else{
                LOGGER.error("Error while trying to refresh access token for user : " + getUser());
                throw new Exception();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public Credential getOAuthCredentialObject() {
            return this.oauthCredentialObject;
        }
    }
}
