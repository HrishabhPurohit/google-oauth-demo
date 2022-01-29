package com.hporg.demo.serviceprovider.api;

import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;

/**
 * @author hrishabh.purohit
 */
public abstract class AbstractServiceProviderAPI {
    
    private String serviceProviderAPIName;

    public AbstractServiceProviderAPI(String serviceProviderAPIName){
        this.serviceProviderAPIName = serviceProviderAPIName;
    }

    public abstract IServiceProviderAPIClientFactory getAPIClientFactory();

    public String getServiceProviderAPIName() {
        return serviceProviderAPIName;
    }
}
