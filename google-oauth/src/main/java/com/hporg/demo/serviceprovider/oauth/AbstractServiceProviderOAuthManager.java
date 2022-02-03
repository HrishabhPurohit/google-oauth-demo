package com.hporg.demo.serviceprovider.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hrishabh.purohit
 */
public abstract class AbstractServiceProviderOAuthManager{
    
    private String approach;
    private List<String> scopes;
    private String user;
    private Map<String, AbstractOAuthToken> userVSAccessTokenCache;
    
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
        this.userVSAccessTokenCache = new HashMap<String, AbstractOAuthToken>();
    }

    public abstract <T> T getOAuthCredentials() throws IOException;

    public AbstractOAuthToken fetchFromTokenCache(String key){ // acts as getter for the cache map userVSAccessTokenCache
        return this.userVSAccessTokenCache.get(key);
    } 

    public void updateTokenCache(String key, AbstractOAuthToken token){ // acts as a setter for cache map userVSAccessTokenCache
        this.userVSAccessTokenCache.put(key, token);
    }

    public abstract class AbstractOAuthToken {
        private String accessToken;
        private Long expirationTimeInMillis;
        
        public String getAccessToken() {
            return accessToken;
        }

        public Long getExpirationTimeInMillis() {
            return expirationTimeInMillis;
        }

        public AbstractOAuthToken (String accessToken, Long expirationTimeInMillis){
            this.accessToken = accessToken;
            this.expirationTimeInMillis = expirationTimeInMillis;
        }

        public abstract boolean isExpired();
    };
}
