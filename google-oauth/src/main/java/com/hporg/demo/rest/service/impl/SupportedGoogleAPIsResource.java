package com.hporg.demo.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hporg.demo.rest.resource.OAuthScope;
import com.hporg.demo.rest.resource.SupportedGoogleAPIs;
import com.hporg.demo.rest.service.GoogleOAuthDemoRestResourceService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * <p> REST Resource for operations on all the supported Google APIs for OAuth demo.
 * <p> NOTE: Only Gmail API is supported for version 1 of demo app.
 */
@Component
@Qualifier("supportedGoogleAPIsResource")
public class SupportedGoogleAPIsResource implements GoogleOAuthDemoRestResourceService <SupportedGoogleAPIs>{
    
    @Autowired
    public ObjectMapper jsonObjectMapper;

    private static final Logger LOGGER = LogManager.getLogger(SupportedGoogleAPIsResource.class);

    @Override
    @SuppressWarnings("unchecked")
    public List<SupportedGoogleAPIs> get() {
        try{
            LOGGER.debug("Attempting to read list of supported APIs from JSON file.");
            return (List<SupportedGoogleAPIs>) jsonObjectMapper.readValue(new ClassPathResource("rest/resources/supportedGoogleAPIs.json").getFile(), List.class);
        } catch (DatabindException dbe){
            LOGGER.error("Exception occured while trying to parse JSON. Falling back to manual process", dbe);

            OAuthScope scope = new OAuthScope();
            scope.setScopeServiceName("https://www.googleapis.com/auth/gmail.readonly");
            scope.setScopeLabel("Read only scope");
            scope.setDescription("Only read operation allowed");
            scope.setAllowedActions(new String [] {"Read Mail"});

            List<OAuthScope> scopes = new ArrayList<OAuthScope>();
            scopes.add(scope);

            SupportedGoogleAPIs apis = new SupportedGoogleAPIs();
            apis.setGoogleAPILabel("Gmail API");
            apis.setGoogleAPIServiceName("gmail.googleapis.com");
            apis.setScopes(scopes);

            List<SupportedGoogleAPIs> returnValue = new ArrayList<SupportedGoogleAPIs>();
            returnValue.add(apis);

            LOGGER.debug("Defaulting to GMail API details : " + apis.toString());

            return returnValue;
        } catch (Exception e){
            LOGGER.error("Exception occurred while reading list of supported APIs", e);
            return null;
        }
    }

    @Override
    public SupportedGoogleAPIs post(String payload) {
        return null;
    }
}
