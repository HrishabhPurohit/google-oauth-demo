package com.hporg.demo.serviceprovider.api.impl;

import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author hrishabh.purohit
 * <p> Gmail specific implementation of <code>AbstractServiceProviderAPI</code>.
 * <p> Primary responsibility is provide a hook to a <code>IServiceProviderAPIClientFactory</code> implementaion for the caller to be able to build an instance of <code>IServiceProviderAPIClient</code>
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
