package com.hporg.demo.serviceprovider.api.impl;

import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.IServiceProviderAPIFactory;
import com.hporg.demo.utils.GoogleOAuthDemoUtil;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 */
@Component
@Scope("singleton")
@Qualifier("serviceProviderAPIFactoryImpl")
public class ServiceProviderAPIFactoryImpl implements IServiceProviderAPIFactory{

    @Override
    public AbstractServiceProviderAPI buildAPI(String apiLabel) {
        return GoogleOAuthDemoUtil.resolveServiceProviderAPIFromAPIName(apiLabel);
    }
}
