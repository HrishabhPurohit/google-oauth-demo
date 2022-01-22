package com.hporg.demo.rest.resource;

import java.util.List;

/**
 * @author hrishabh.purohit
 * <p> POJO for representing Google API details
 */
public class SupportedGoogleAPIs {

    private String googleAPIServiceName;
    private String googleAPILabel;
    private List<OAuthScope> scopes;
    
    public List<OAuthScope> getScopes() {
        return scopes;
    }
    public void setScopes(List<OAuthScope> scopes) {
        this.scopes = scopes;
    }
    public String getGoogleAPIServiceName() {
        return googleAPIServiceName;
    }
    public void setGoogleAPIServiceName(String googleAPIServiceName) {
        this.googleAPIServiceName = googleAPIServiceName;
    }
    public String getGoogleAPILabel() {
        return googleAPILabel;
    }
    public void setGoogleAPILabel(String googleAPILabel) {
        this.googleAPILabel = googleAPILabel;
    }

    @Override
    public String toString() {
        return "Google API Service Name : " + this.getGoogleAPIServiceName() + " , API Label : " + this.getGoogleAPILabel() + ", OAuth Scopes : " + scopes.toString();
    }
}
