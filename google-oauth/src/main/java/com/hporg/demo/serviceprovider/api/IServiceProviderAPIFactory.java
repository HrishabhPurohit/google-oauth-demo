package com.hporg.demo.serviceprovider.api;

/**
 * @author hrishabh.purohit
 */
public interface IServiceProviderAPIFactory {
    
    public AbstractServiceProviderAPI buildAPI(String apiLabel);
}
