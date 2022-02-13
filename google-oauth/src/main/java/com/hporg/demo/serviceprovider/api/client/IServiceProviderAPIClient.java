package com.hporg.demo.serviceprovider.api.client;

import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;

/**
 * @author hrishabh.purohit
 * @param <T> T - Type of client service object that the client specific methods use to perform user actions.
 * @see GmailAPIClient
 */
public interface IServiceProviderAPIClient<T>{
    public void setUserForOperation(String user);
    public String resolveUserActionToClientActionAndExecute(String userAction, T apiClientService) throws Exception;
    public T buildAPIClientServiceObject(AbstractOAuthToken oauthCredential) throws Exception;
}
