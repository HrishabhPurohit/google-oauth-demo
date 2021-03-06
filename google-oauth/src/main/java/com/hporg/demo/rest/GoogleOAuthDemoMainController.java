package com.hporg.demo.rest;

import java.util.List;

import com.hporg.demo.rest.resource.GoogleAPIActionResponse;
import com.hporg.demo.rest.resource.SupportedGoogleAPIs;
import com.hporg.demo.rest.service.GoogleOAuthDemoRestResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hrishabh.purohit
 * <p>This class acts as the master controller. Responsibilties:
 * <p>1. Request navigation to specific api version resources.
 * <p>2. Param and request validation.
 * <p>3. Responding with meaningful http status codes and corresponding results.
 */
@RestController
@RequestMapping(value = "/api/demo/google-oauth")
public class GoogleOAuthDemoMainController {
    
    @Autowired
    @Qualifier("supportedGoogleAPIsResource")
    GoogleOAuthDemoRestResourceService<SupportedGoogleAPIs> supportedAPIsRestResourceService;

    @Autowired
    @Qualifier("googleAPIOperationResponseRestResource")
    GoogleOAuthDemoRestResourceService<GoogleAPIActionResponse> googleAPIResponseRestResourceService;

    // do not put space in text string for headers.
    @GetMapping(value = "/supportedGoogleAPIs" ,headers = "X-API-VERSION=1", produces = "application/json")
    public List<SupportedGoogleAPIs> getAllSupportedAPIsV1(){
        return supportedAPIsRestResourceService.get();
    }
    
    @PostMapping(value = "/action", headers = "X-API-VERSION=1", consumes = "application/json")
    public ResponseEntity<String> performActionV1(@RequestBody String payload){
        GoogleAPIActionResponse response = googleAPIResponseRestResourceService.post(payload);
        
        return ResponseEntity.status(response.getStatusCode()).body(response.getResult());
    }
}
