package com.hporg.demo.serviceprovider.api.impl;

import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * <p> Gmail specific implementation of <code>AbstractServiceProviderAPI</code>.
 * <p> Primary responsibility is provide a hook to a <code>IServiceProviderAPIClientFactory</code> implementaion for the caller to be able to build an instance of <code>IServiceProviderAPIClient</code>
 */
@Component
@Qualifier("gmailServiceProviderAPI")
public class GmailServiceProviderAPI extends AbstractServiceProviderAPI{

    @Autowired
    @Qualifier("serviceProviderAPIClientFactoryImpl")
    private IServiceProviderAPIClientFactory serviceProviderAPIClientFactory;

    @Autowired
    public GmailServiceProviderAPI(@Value("#{T(com.hporg.demo.utils.EServiceProviderAPIs).GMAIL.getApiName()}") String apiName){
        super(apiName);
    }

    @Override
    public IServiceProviderAPIClientFactory getAPIClientFactory() {
        return serviceProviderAPIClientFactory;
    }
}
