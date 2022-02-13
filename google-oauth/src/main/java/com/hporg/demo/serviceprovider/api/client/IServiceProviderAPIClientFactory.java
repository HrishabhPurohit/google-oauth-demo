package com.hporg.demo.serviceprovider.api.client;

/**
 * @author hrishabh.purohit
 * @see ServiceProviderAPIClientFactoryImpl
 */
public interface IServiceProviderAPIClientFactory {

    public <T> IServiceProviderAPIClient<T> buildAPIClient(String serviceProviderAPIName);
}
