package com.hporg.demo.serviceprovider;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.hporg.demo.rest.resource.GoogleAPIActionResponse;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;

/**
 * @author hrishabh.purohit
 */
public class ServiceProviderRequestExecutor {

    @SuppressWarnings("unchecked")
    public static GoogleAPIActionResponse execute(ServiceProviderRequest serviceProviderRequest, String action) {
        GoogleAPIActionResponse apiResponse = null;

        try {
            IServiceProviderAPIClient apiClient = serviceProviderRequest.getApiClient();
            AbstractOAuthToken oauthCreds = serviceProviderRequest.getServiceProvider().getOauthManager().getOAuthCredentials();
            String response = apiClient.resolveUserActionToClientActionAndExecute(action, apiClient.buildAPIClientServiceObject(oauthCreds));
            
            apiResponse = new GoogleAPIActionResponse();
            apiResponse.setResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return apiResponse;
    }
}
