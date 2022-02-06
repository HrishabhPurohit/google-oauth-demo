package com.hporg.demo.serviceprovider.oauth;

import java.io.IOException;
import java.util.List;

/**
 * @author hrishabh.purohit
 */
public abstract class AbstractServiceProviderOAuthManager{
    
    private String approach;
    private List<String> scopes;
    private String user;
    
    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApproach() {
        return this.approach;
    }

    public AbstractServiceProviderOAuthManager(String approach){
        this.approach = approach;
    }

    public abstract AbstractOAuthToken getOAuthCredentials() throws IOException;

    public abstract class AbstractOAuthToken {
        private String accessToken;
        private Long expirationTimeInMillis;
        
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public void setExpirationTimeInMillis(Long expirationTimeInMillis) {
            this.expirationTimeInMillis = expirationTimeInMillis;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public Long getExpirationTimeInMillis() {
            return expirationTimeInMillis;
        }

        public abstract boolean isExpired();

        public abstract void refreshToken() throws IOException;

        public abstract <T> T getOAuthCredentialObject();
    };
}
