package com.hporg.demo.serviceprovider.api;

/**
 * @author hrishabh.purohit
 * @see ServiceProviderAPIFactoryImpl
 */
public interface IServiceProviderAPIFactory {
    
    public AbstractServiceProviderAPI buildAPI(String apiLabel);
}
