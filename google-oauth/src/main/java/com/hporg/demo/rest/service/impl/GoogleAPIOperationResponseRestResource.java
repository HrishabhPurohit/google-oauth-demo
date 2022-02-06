package com.hporg.demo.rest.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hporg.demo.rest.resource.GoogleAPIActionResponse;
import com.hporg.demo.rest.service.GoogleOAuthDemoRestResourceService;
import com.hporg.demo.serviceprovider.ServiceProviderRequest;
import com.hporg.demo.serviceprovider.ServiceProviderRequestExecutor;
import com.hporg.demo.utils.GoogleOAuthDemoUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * Responsible for:
 * <p> 1. Extracting details from JSON payload necessary for the desired email operation.
 */
@Component("googleAPIOperationResponseRestResource")
public class GoogleAPIOperationResponseRestResource implements GoogleOAuthDemoRestResourceService <GoogleAPIActionResponse>{

    private static final Logger LOGGER = LogManager.getLogger(GoogleAPIOperationResponseRestResource.class);

    private String userForOperation;
    private String actionForOperation;
    private List<String> scopeForOperation;
    private String apiForOperation;

    @SuppressWarnings("unchecked")
    private void populateOperationFields(String payload) throws Exception{
        LOGGER.debug("Inside populateOperationFields method.");
        
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        Map<String, List<String>> jsonPayload = (Map<String, List<String>>) jsonObjectMapper.readValue(payload, Map.class);

        this.userForOperation = jsonPayload.get("user").get(0);
        this.actionForOperation = jsonPayload.get("action").get(0);
        this.scopeForOperation = jsonPayload.get("scopes");
        this.apiForOperation = jsonPayload.get("api").get(0);

        LOGGER.debug("Extracted details from the payload : " + this.toString());
    }

    @Override
    public List<GoogleAPIActionResponse> get() {
        return null;
    }

    @Override
    public GoogleAPIActionResponse post(String payload) {
        try{
            LOGGER.debug("Payload extraction initiated.");
            populateOperationFields(payload);
        } catch (Exception e){
            LOGGER.error("Exception occurred while trying to extract details from payload : ",e);
            throw new IllegalArgumentException(e.getMessage());
        }

        GoogleAPIActionResponse response = null;

        try {
            if(GoogleOAuthDemoUtil.isSPRCached(generateKeyForSPRCache())){
                response = GoogleOAuthDemoUtil.getSPRFromCache(generateKeyForSPRCache());
            }
            else{
                ServiceProviderRequest spRequest = new ServiceProviderRequest.RequestBuilder()
                                                                            .withServiceProvider(GoogleOAuthDemoUtil.resolveServiceProviderFromUserName(this.userForOperation))
                                                                            .withAPI(this.apiForOperation)
                                                                            .withScopes(this.scopeForOperation)
                                                                            .withUser(this.userForOperation)
                                                                            .withClient()
                                                                            .build();

                response = ServiceProviderRequestExecutor.execute(spRequest, actionForOperation);
                GoogleOAuthDemoUtil.initRequestCaching(generateKeyForSPRCache(), response);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return response;
    }
    
    @Override
    public String toString() {
        return "User : " + this.userForOperation + ", Action : " + this.actionForOperation + ", Scope : " + this.scopeForOperation + ", API : " + this.apiForOperation;
    }

    private String generateKeyForSPRCache(){
        return this.userForOperation + "|" + this.apiForOperation + "|" + this.actionForOperation;
    }
}
