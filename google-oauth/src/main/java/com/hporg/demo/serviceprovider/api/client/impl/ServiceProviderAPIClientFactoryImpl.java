package com.hporg.demo.serviceprovider.api.client.impl;

import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClientFactory;
import com.hporg.demo.utils.GoogleOAuthDemoUtil;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
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