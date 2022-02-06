package com.hporg.demo.serviceprovider.api.client;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;

public interface IServiceProviderAPIClient<T>{
    public void setUserForOperation(String user);
    public String resolveUserActionToClientActionAndExecute(String userAction, T apiClientService);
    public T buildAPIClientServiceObject(AbstractOAuthToken oauthCredential) throws GeneralSecurityException, IOException;
}
