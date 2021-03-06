package com.hporg.demo.serviceprovider;

import java.util.ArrayList;
import java.util.List;

import com.hporg.demo.rest.resource.GoogleAPIActionResponse;
import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author hrishabh.purohit
 */
@SuppressWarnings({"unused", "rawtypes"})
public final class ServiceProviderRequest {

    private final AbstractServiceProvider serviceProvider;
    private final AbstractServiceProviderAPI api;
    private final IServiceProviderAPIClient apiClient;
    private final List<String> scopes;
    private final String user;

    private ServiceProviderRequest(RequestBuilder builder) {
        this.serviceProvider = builder.serviceProvider;
        this.api = builder.api;
        this.apiClient = builder.apiClient;
        this.user = builder.user;

        this.scopes = new ArrayList<String>();
        for (String scope : builder.scopes) {
            this.scopes.add(scope);
        }
    }

    public static final class RequestBuilder {
        private AbstractServiceProvider serviceProvider;
        private AbstractServiceProviderAPI api;
        private IServiceProviderAPIClient apiClient;
        private List<String> scopes;
        private String user;

        public RequestBuilder withServiceProvider(AbstractServiceProvider serviceProvider) {
            this.serviceProvider = serviceProvider;
            return this;
        }

        public RequestBuilder withAPI(String api) {
            if(this.serviceProvider == null){
                throw new IllegalArgumentException("ERROR: Caller attempted to build API without having a Service Provider.");
            }
            this.api = this.serviceProvider
                    .getAPIFactory()
                    .buildAPI(api);
            return this;
        }

        public RequestBuilder withScopes(List<String> scopes) {
            if(this.serviceProvider.getOauthManager() == null){
                throw new IllegalArgumentException("ERROR: No OAuth manager found to set the scopes for");
            }
            this.scopes = scopes;
            this.serviceProvider.getOauthManager().setScopes(scopes);
            return this;
        }

        public RequestBuilder withClient() {
            if(this.api == null){
                throw new IllegalArgumentException("ERROR: Caller attempted to build API Client without having an API");
            }
            this.apiClient = this.api
                    .getAPIClientFactory()
                    .buildAPIClient(this.api.getServiceProviderAPIName());
            return this;
        }

        public RequestBuilder withUser(String user){
            if(this.serviceProvider.getOauthManager() == null || this.apiClient == null){
                throw new IllegalArgumentException("ERROR: No OAuth manager OR API client found to set the user for");
            }
            this.user = user;
            this.serviceProvider.getOauthManager().setUser(user);
            this.apiClient.setUserForOperation(user);
            return this;
        }

        public ServiceProviderRequest build() {
            ServiceProviderRequest serviceProviderRequest = new ServiceProviderRequest(this);
            return serviceProviderRequest;
        }
    }

    public AbstractServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public AbstractServiceProviderAPI getApi() {
        return api;
    }

    public IServiceProviderAPIClient getApiClient() {
        return apiClient;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "SERVICE PROVIDER REQUEST : [ USER : " + this.user + " | SERVICE PROVIDER : " + this.serviceProvider.toString() + " | API : " + this.api.toString() + " | SCOPE : " + this.scopes.toString();
    }
}
