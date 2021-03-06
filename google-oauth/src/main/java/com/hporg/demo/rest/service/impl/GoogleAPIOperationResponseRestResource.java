package com.hporg.demo.rest.service.impl;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * Responsible for:
 * <p> 1. Extracting details from JSON payload necessary for the desired email operation.
 * <p> 2. Proper exception handling.
 * <p> 3. User request caching. In case same user requests for the same action, cached response should be returned.
 */
@Component("googleAPIOperationResponseRestResource")
public class GoogleAPIOperationResponseRestResource implements GoogleOAuthDemoRestResourceService <GoogleAPIActionResponse>{

    private static final Logger LOGGER = LogManager.getLogger(GoogleAPIOperationResponseRestResource.class);

    private String userForOperation;
    private String actionForOperation;
    private List<String> scopeForOperation;
    private String apiForOperation;
    
    @Value("${google.oauth.demo.app.request.caching}")
    private boolean isAppRequestCachingEnabled;
    
    @SuppressWarnings("unchecked")
    private void populateOperationFields(String payload) throws Exception{
        LOGGER.debug("Inside populateOperationFields method.");
        
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        Map<String, Object> jsonPayload = (Map<String, Object>) jsonObjectMapper.readValue(payload, Map.class);

        this.userForOperation = (String) jsonPayload.get("user");
        this.actionForOperation = (String) jsonPayload.get("action");
        this.scopeForOperation = (List<String>) jsonPayload.get("scopes");
        this.apiForOperation = (String) jsonPayload.get("api");

        LOGGER.debug("Extracted details from the payload : " + this.toString());
    }

    @Override
    public List<GoogleAPIActionResponse> get() {
        return null;
    }

    @Override
    public GoogleAPIActionResponse post(String payload) {
        GoogleAPIActionResponse response = null;

        try{
            LOGGER.debug("Payload extraction initiated.");
            populateOperationFields(payload);
        } catch (Exception e){
            LOGGER.error("Exception occurred while trying to extract details from payload : ",e);
            response = new GoogleAPIActionResponse();
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            response.setResult("");
            return response;
        }

        Boolean isSPRCached = GoogleOAuthDemoUtil.isSPRCached(generateKeyForSPRCache());

        if(isAppRequestCachingEnabled && isSPRCached != null && isSPRCached){
            response = GoogleOAuthDemoUtil.getSPRFromCache(generateKeyForSPRCache());
        }
        else{
            try{
                ServiceProviderRequest spRequest = new ServiceProviderRequest.RequestBuilder()
                                                                            .withServiceProvider(GoogleOAuthDemoUtil.resolveServiceProviderFromUserName(this.userForOperation))
                                                                            .withAPI(this.apiForOperation)
                                                                            .withClient()
                                                                            .withScopes(this.scopeForOperation)
                                                                            .withUser(this.userForOperation)
                                                                            .build();

                String apiResponse = ServiceProviderRequestExecutor.execute(spRequest, actionForOperation);
                response = new GoogleAPIActionResponse();
                response.setResult(apiResponse);
                response.setStatusCode(HttpStatus.OK);

                GoogleOAuthDemoUtil.initRequestCaching(generateKeyForSPRCache(), response);
            } catch(IOException e){
                LOGGER.error("Exception occurred.",e);
                response = new GoogleAPIActionResponse();
                response.setResult(e.getMessage());
                response.setStatusCode(HttpStatus.FOUND);
            } catch (IllegalArgumentException e){
                LOGGER.error("Exception occurred.",e);
                response = new GoogleAPIActionResponse();
                response.setResult(e.getMessage());
                response.setStatusCode(HttpStatus.BAD_REQUEST);
            } catch (Exception e){
                LOGGER.error("Exception occurred.",e);
                response = new GoogleAPIActionResponse();
                response.setResult(e.getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
