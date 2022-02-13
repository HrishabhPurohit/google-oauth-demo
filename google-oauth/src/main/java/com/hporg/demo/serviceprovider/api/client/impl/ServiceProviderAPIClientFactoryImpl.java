package com.hporg.demo.serviceprovider.api.client.impl;

import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;
import com.hporg.demo.utils.GoogleOAuthDemoUtil;

import org.springframework.beans.factory.annotation.Qualifier;
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

    @Override
    @SuppressWarnings("unchecked")
    public IServiceProviderAPIClient<?> buildAPIClient(String serviceProviderAPIName) {
        return GoogleOAuthDemoUtil.resolveAPIClientFromAPIName(serviceProviderAPIName);
    }
}