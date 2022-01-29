package com.hporg.demo.serviceprovider.oauth;

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

    public abstract <T> T getOAuthCredentials();
}
