package com.hporg.demo.utils;

import java.util.HashMap;
import java.util.Map;

import com.hporg.demo.serviceprovider.AbstractServiceProvider;
import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.api.client.impl.GmailAPIClient;
import com.hporg.demo.serviceprovider.api.impl.GmailServiceProviderAPI;
import com.hporg.demo.serviceprovider.impl.GoogleServiceProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author hrishabh.purohit
 */
public class GoogleOAuthDemoUtil {
    
    private static final Logger LOGGER = LogManager.getLogger(GoogleOAuthDemoUtil.class);
    private static Map<String, AbstractServiceProvider> domainVsServiceProvider;
    private static Map<String, AbstractServiceProviderAPI> apiNameVsServiceProviderAPI;
    private static Map<String, IServiceProviderAPIClient> apiNameVsServiceProviderAPIClient;

    static{
        LOGGER.debug("Initializing domain vs service provider cache");

        domainVsServiceProvider = new HashMap<String, AbstractServiceProvider>();
        domainVsServiceProvider.put(EServiceProviders.GMAIL.getDomainName(), new GoogleServiceProvider(EServiceProviders.GMAIL.getDomainName()));

        LOGGER.debug("Successfully initialized domain vs service provider cache");
        LOGGER.debug("Initializing name vs service provider api cache");

        apiNameVsServiceProviderAPI = new HashMap<String, AbstractServiceProviderAPI>();
        apiNameVsServiceProviderAPI.put(EServiceProviderAPIs.GMAIL.getApiName(), new GmailServiceProviderAPI(EServiceProviderAPIs.GMAIL.getApiName()));

        LOGGER.debug("Successfully initialized name vs service provider api cache");
        LOGGER.debug("Initializing name vs service provider api client cache");

        apiNameVsServiceProviderAPIClient = new HashMap<String, IServiceProviderAPIClient>();
        apiNameVsServiceProviderAPIClient.put(EServiceProviderAPIs.GMAIL.getApiName(), new GmailAPIClient());
    }
    
    public static AbstractServiceProvider resolveServiceProviderFromDomainName(String domainName){
        return domainVsServiceProvider.get(domainName);
    }

    public static AbstractServiceProviderAPI resolveServiceProviderAPIFromAPIName(String apiName){
        return apiNameVsServiceProviderAPI.get(apiName);
    }

    public static IServiceProviderAPIClient resolveAPIClientFromAPIName(String apiName){
        return apiNameVsServiceProviderAPIClient.get(apiName);
    }
}
