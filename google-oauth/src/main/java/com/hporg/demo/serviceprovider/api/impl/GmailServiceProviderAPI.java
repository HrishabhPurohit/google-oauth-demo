package com.hporg.demo.serviceprovider.api.impl;

import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author hrishabh.purohit
 */
public class GmailServiceProviderAPI extends AbstractServiceProviderAPI{

    @Autowired
    @Qualifier("serviceProviderAPIClientFactoryImpl")
    private IServiceProviderAPIClientFactory serviceProviderAPIClientFactory;

    public GmailServiceProviderAPI(String apiName){
        super(apiName);
    }

    @Override
    public IServiceProviderAPIClientFactory getAPIClientFactory() {
        return serviceProviderAPIClientFactory;
    }
}
