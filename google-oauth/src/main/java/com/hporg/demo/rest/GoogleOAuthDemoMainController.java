package com.hporg.demo.rest;

import java.util.List;

import com.hporg.demo.model.SupportedGoogleAPIs;
import com.hporg.demo.rest.service.SupportedGoogleAPIsResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hrishabh.purohit
 * <p>This class acts as the master controller. Responsibilties:
 * <p>1. Request navigation to specific api version resources.
 * <p>2. Param and request validation.
 */
@RestController
@RequestMapping(value = "/api/demo/google-oauth")
public class GoogleOAuthDemoMainController {
    
    @Autowired
    SupportedGoogleAPIsResource apiService;

    // do not put space in text string for headers.
    @GetMapping(value = "/supportedGoogleAPIs" ,headers = "X-API-VERSION=1", produces = "application/json")
    public List<SupportedGoogleAPIs> getAllSuppoertedAPIs(){
        return apiService.getAllSuppoertedAPIsV1();
    }
    
}
