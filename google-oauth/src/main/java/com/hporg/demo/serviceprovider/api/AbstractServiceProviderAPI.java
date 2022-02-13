package com.hporg.demo.serviceprovider.api;

import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;

/**
 * @author hrishabh.purohit
 * @see GmailServiceProviderAPI
 */
public abstract class AbstractServiceProviderAPI {
    
    private String serviceProviderAPIName;

    @Override
    public String toString() {
        return "SERVICE PROVIDER API : " + this.serviceProviderAPIName;
    }

    public AbstractServiceProviderAPI(String serviceProviderAPIName){
        this.serviceProviderAPIName = serviceProviderAPIName;
    }

    public abstract IServiceProviderAPIClientFactory getAPIClientFactory();

    public String getServiceProviderAPIName() {
        return serviceProviderAPIName;
    }
}
