package com.hporg.demo.serviceprovider.impl;

import com.hporg.demo.serviceprovider.AbstractServiceProvider;
import com.hporg.demo.serviceprovider.api.IServiceProviderAPIFactory;
import com.hporg.demo.utils.EServiceProviders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * <p> Google specific Implementation for <code>AbstractServiceProvider</code>
 * <p> Primary responsibilities include:
 * <p> 1. Providing a hook to <code>IServiceProviderAPIFactory</code> implementation for the caller to build required API instatnce.
 * <p> 2. Providing a <code>AbstractServiceProviderOAuthManager</code> implementation for the caller to execute the end client's request.
 */
@Component
public class GoogleServiceProvider extends AbstractServiceProvider{

    @Autowired
    @Qualifier("serviceProviderAPIFactoryImpl")
    private IServiceProviderAPIFactory serviceProviderAPIFActory;

    @Autowired
    public GoogleServiceProvider(@Value("#{T(com.hporg.demo.utils.EServiceProviders).GMAIL.getDomainName()}") String domainName){
        super(domainName);
    }

    @Override
    public IServiceProviderAPIFactory getAPIFactory() {
        return serviceProviderAPIFActory;
    }
    
}
