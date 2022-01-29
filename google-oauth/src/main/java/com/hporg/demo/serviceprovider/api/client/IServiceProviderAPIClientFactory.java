package com.hporg.demo.serviceprovider.api.client;

/**
 * @author hrishabh.purohit
 */
public interface IServiceProviderAPIClientFactory {

    public IServiceProviderAPIClient buildAPIClient(String serviceProviderAPIName);
}
