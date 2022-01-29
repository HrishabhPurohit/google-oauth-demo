package com.hporg.demo.serviceprovider;

import com.hporg.demo.serviceprovider.api.IServiceProviderAPIFactory;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager;

/**
 * @author hrishabh.purohit
 */
public abstract class AbstractServiceProvider {

    private String providerNameLabel;
    private AbstractServiceProviderOAuthManager oauthManager;
    
    public AbstractServiceProvider(String providerName){
        this.providerNameLabel = providerName;
    }

    public AbstractServiceProviderOAuthManager getOauthManager() {
        return oauthManager;
    }

    public void setOauthManager(AbstractServiceProviderOAuthManager oauthManager) {
        this.oauthManager = oauthManager;
    }

    public String getProviderNameLabel() {
        return providerNameLabel;
    }

    public abstract IServiceProviderAPIFactory getAPIFactory();
}
