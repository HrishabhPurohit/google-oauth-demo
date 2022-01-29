package com.hporg.demo.serviceprovider.oauth.impl;


import com.google.api.services.gmail.Gmail;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 */
@Component
@Qualifier("serviceProviderOAuthManager")
public class GmailOAuthManagerImpl extends AbstractServiceProviderOAuthManager{

    @Autowired
    public GmailOAuthManagerImpl(@Value("${google.oauth.approach}") String approach) {
        super(approach);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Gmail getOAuthCredentials() {
        return null;
    }

    public class TokenCache{
        
    }
}
