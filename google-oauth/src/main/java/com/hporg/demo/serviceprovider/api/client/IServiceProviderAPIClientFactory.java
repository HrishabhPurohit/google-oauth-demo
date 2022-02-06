package com.hporg.demo.serviceprovider.api.client;

/**
 * @author hrishabh.purohit
 */
public interface IServiceProviderAPIClientFactory {

    public <T> IServiceProviderAPIClient<T> buildAPIClient(String serviceProviderAPIName);
}
