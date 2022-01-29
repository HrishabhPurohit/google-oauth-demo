package com.hporg.demo.serviceprovider.impl;

import com.hporg.demo.serviceprovider.AbstractServiceProvider;
import com.hporg.demo.serviceprovider.api.IServiceProviderAPIFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author hrishabh.purohit
 */
public class GoogleServiceProvider extends AbstractServiceProvider{

    private static final Logger LOGGER = LogManager.getLogger(GoogleServiceProvider.class);

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
