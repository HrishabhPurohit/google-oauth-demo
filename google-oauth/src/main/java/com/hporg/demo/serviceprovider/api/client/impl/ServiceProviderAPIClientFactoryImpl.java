package com.hporg.demo.serviceprovider.api.client.impl;

import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;
import com.hporg.demo.utils.EServiceProviderAPIs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * <p> Abstract factory implementation for <code>IServiceProviderAPIClientFactory</code>
 * <p> Primary responsibility is to provide a way to build an instance of <code>IServiceProviderAPIClient</code> for the caller to execute client operations specific to the API.
 */
@Component
@Scope("singleton")
@Qualifier("serviceProviderAPIClientFactoryImpl")
public class ServiceProviderAPIClientFactoryImpl implements IServiceProviderAPIClientFactory{

    @Autowired
    private ApplicationContext appContext;

    @Override
    @SuppressWarnings("unchecked")
    public IServiceProviderAPIClient<?> buildAPIClient(String serviceProviderAPIName) {
        IServiceProviderAPIClient<?> client = null;

        if(serviceProviderAPIName.equals(EServiceProviderAPIs.GMAIL.getApiName())){
            client = (IServiceProviderAPIClient<?>) appContext.getBean("gmailAPIClient");
        }

        return client;
    }
}