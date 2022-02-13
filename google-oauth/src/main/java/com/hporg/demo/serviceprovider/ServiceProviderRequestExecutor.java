package com.hporg.demo.serviceprovider;

import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;
import com.hporg.demo.utils.GoogleOAuthDemoUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author hrishabh.purohit
 */
public class ServiceProviderRequestExecutor {

    private static final Logger LOGGER = LogManager.getLogger(ServiceProviderRequestExecutor.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String execute(ServiceProviderRequest serviceProviderRequest, String action) throws Exception{
        LOGGER.debug("Executing : " + serviceProviderRequest.toString());

        IServiceProviderAPIClient apiClient = serviceProviderRequest.getApiClient();
        AbstractOAuthToken oauthCreds = serviceProviderRequest.getServiceProvider()
                                                            .getOauthManager()
                                                            .getOAuthCredentials();

        GoogleOAuthDemoUtil.initTokenCaching(serviceProviderRequest.getUser(), oauthCreds);

        String response = apiClient.resolveUserActionToClientActionAndExecute(action, apiClient.buildAPIClientServiceObject(oauthCreds));
        return response;
    }
}
