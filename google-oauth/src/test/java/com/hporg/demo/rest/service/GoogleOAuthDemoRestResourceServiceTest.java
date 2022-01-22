package com.hporg.demo.rest.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.notNull;

import java.util.List;

import com.hporg.demo.rest.resource.SupportedGoogleAPIs;
import com.hporg.demo.rest.service.impl.SupportedGoogleAPIsResource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class GoogleOAuthDemoRestResourceServiceTest {
    
    @Autowired
    @Qualifier("supportedGoogleAPIsResource")
    SupportedGoogleAPIsResource service;

    @Test
    public void serviceGetTest(){
        assertNotNull(service.jsonObjectMapper);

        List<SupportedGoogleAPIs> list = service.get();

        assertNotNull(list);
        assertTrue(list.size() != 0);
    }
}
