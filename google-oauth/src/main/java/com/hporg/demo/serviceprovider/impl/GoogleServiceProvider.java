package com.hporg.demo.serviceprovider.impl;

import com.hporg.demo.serviceprovider.AbstractServiceProvider;
import com.hporg.demo.serviceprovider.api.IServiceProviderAPIFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author hrishabh.purohit
 * <p> Google specific Implementation for <code>AbstractServiceProvider</code>
 * <p> Primary responsibilities include:
 * <p> 1. Providing a hook to <code>IServiceProviderAPIFactory</code> implementation for the caller to build required API instatnce.
 * <p> 2. Providing a <code>AbstractServiceProviderOAuthManager</code> implementation for the caller to execute the end client's request.
 */
public class GoogleServiceProvider extends AbstractServiceProvider{

    @Autowired
    @Qualifier("serviceProviderAPIFactoryImpl")
    private IServiceProviderAPIFactory serviceProviderAPIFActory;

    public GoogleServiceProvider(String domainName){
        super(domainName);
    }

    @Override
    public IServiceProviderAPIFactory getAPIFactory() {
        return serviceProviderAPIFActory;
    }
    
}
