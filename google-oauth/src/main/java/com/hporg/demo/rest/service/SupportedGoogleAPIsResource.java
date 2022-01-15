package com.hporg.demo.rest.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hporg.demo.model.SupportedGoogleAPIs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author hrishabh.purohit
 * <p> REST Resource for operations on all the supported Google APIs for OAuth demo.
 * <p> NOTE: Only Gmail API is supported for version 1 of demo app.
 */
@Component
public class SupportedGoogleAPIsResource {
    
    @Autowired
    ObjectMapper jsonObjectMapper;

    private static final Logger LOGGER = LogManager.getLogger(SupportedGoogleAPIsResource.class);

    public List<SupportedGoogleAPIs> getAllSuppoertedAPIsV1(){

        try{
            LOGGER.debug("Attempting to read list of supported APIs from JSON file.");
            return (List<SupportedGoogleAPIs>) jsonObjectMapper.readValue(new ClassPathResource("rest/resources/supportedGoogleAPIs.json").getFile(), List.class);
        } catch (DatabindException dbe){
            LOGGER.error("Exception occured while trying to parse JSON. Falling back to manual process", dbe);

            SupportedGoogleAPIs apis = new SupportedGoogleAPIs();
            apis.setGoogleAPILabel("Gmail API");
            apis.setGoogleAPIServiceName("gmail.googleapis.com");

            List<SupportedGoogleAPIs> returnValue = new ArrayList<SupportedGoogleAPIs>();
            returnValue.add(apis);

            return returnValue;
        } catch (Exception e){
            LOGGER.error("Exception occurred while reading list of supported APIs", e);
            return null;
        }
    }
}
