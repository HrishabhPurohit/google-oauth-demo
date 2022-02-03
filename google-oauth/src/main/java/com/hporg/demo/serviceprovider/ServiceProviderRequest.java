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
@SuppressWarnings("unused")
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

    public class RequestBuilder {
        private AbstractServiceProvider serviceProvider;
        private AbstractServiceProviderAPI api;
        private IServiceProviderAPIClient apiClient;
        private List<String> scopes;
        private String user;

        @Autowired
        @Qualifier("serviceProviderOAuthManager")
        private AbstractServiceProviderOAuthManager oauthManager;

        public RequestBuilder withServiceProvider(AbstractServiceProvider serviceProvider) {
            this.serviceProvider = serviceProvider;
            return this;
        }

        public RequestBuilder withAPI(String api) {
            this.api = this.serviceProvider
                    .getAPIFactory()
                    .buildAPI(api);
            return this;
        }

        public RequestBuilder withScopes(List<String> scopes) {
            this.scopes = scopes;
            this.oauthManager.setScopes(scopes);
            return this;
        }

        public RequestBuilder withClient() {
            this.apiClient = this.api
                    .getAPIClientFactory()
                    .buildAPIClient(this.api.getServiceProviderAPIName());
            return this;
        }

        public RequestBuilder withUser(String user){
            this.user = user;
            this.oauthManager.setUser(user);
            return this;
        }

        public ServiceProviderRequest build() {
            this.serviceProvider.setOauthManager(oauthManager);
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
}
