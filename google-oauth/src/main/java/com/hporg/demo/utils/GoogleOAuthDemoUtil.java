package com.hporg.demo.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.api.client.util.Value;
import com.hporg.demo.rest.resource.GoogleAPIActionResponse;
import com.hporg.demo.serviceprovider.AbstractServiceProvider;
import com.hporg.demo.serviceprovider.api.AbstractServiceProviderAPI;
import com.hporg.demo.serviceprovider.api.client.IServiceProviderAPIClient;
import com.hporg.demo.serviceprovider.api.client.impl.GmailAPIClient;
import com.hporg.demo.serviceprovider.api.impl.GmailServiceProviderAPI;
import com.hporg.demo.serviceprovider.impl.GoogleServiceProvider;
import com.hporg.demo.serviceprovider.oauth.AbstractServiceProviderOAuthManager.AbstractOAuthToken;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hrishabh.purohit
 */
public class GoogleOAuthDemoUtil {
    
    private static final Logger LOGGER = LogManager.getLogger(GoogleOAuthDemoUtil.class);
    private static Map<String, AbstractServiceProvider> domainVsServiceProvider;
    private static Map<String, AbstractServiceProviderAPI> apiNameVsServiceProviderAPI;
    private static Map<String, IServiceProviderAPIClient<?>> apiNameVsServiceProviderAPIClient;
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);
    private static Map<String, GoogleAPIActionResponse> serviceProviderRequestCache;
    private static Map<String, AbstractOAuthToken> oauthTokenCache;

    @Autowired
    @Value("${google.oauth.approach.credfile.path}")
    private static String CRED_FILE_PATH;

    static{
        LOGGER.debug("Initializing domain vs service provider cache");

        domainVsServiceProvider = new HashMap<String, AbstractServiceProvider>();
        domainVsServiceProvider.put(EServiceProviders.GMAIL.getDomainName(), new GoogleServiceProvider(EServiceProviders.GMAIL.getDomainName()));

        LOGGER.debug("Successfully initialized domain vs service provider cache");
        LOGGER.debug("Initializing name vs service provider api cache");

        apiNameVsServiceProviderAPI = new HashMap<String, AbstractServiceProviderAPI>();
        apiNameVsServiceProviderAPI.put(EServiceProviderAPIs.GMAIL.getApiName(), new GmailServiceProviderAPI(EServiceProviderAPIs.GMAIL.getApiName()));

        LOGGER.debug("Successfully initialized name vs service provider api cache");
        LOGGER.debug("Initializing name vs service provider api client cache");

        apiNameVsServiceProviderAPIClient = new HashMap<String, IServiceProviderAPIClient<?>>();
        apiNameVsServiceProviderAPIClient.put(EServiceProviderAPIs.GMAIL.getApiName(), new GmailAPIClient());

        serviceProviderRequestCache = new ConcurrentHashMap<String, GoogleAPIActionResponse>();
        oauthTokenCache = new ConcurrentHashMap<String, AbstractOAuthToken>();
    }
    
    public static AbstractServiceProvider resolveServiceProviderFromUserName(String userName){
        String domainName = null;

        if(userName != null && !userName.equals("") && userName.indexOf("@") != -1){
            domainName = userName.substring(userName.indexOf("@") + 1);
        }
        
        return domainVsServiceProvider.get(domainName);
    }

    public static AbstractServiceProviderAPI resolveServiceProviderAPIFromAPIName(String apiName){
        return apiNameVsServiceProviderAPI.get(apiName);
    }

    public static IServiceProviderAPIClient<?> resolveAPIClientFromAPIName(String apiName){
        return apiNameVsServiceProviderAPIClient.get(apiName);
    }

    public static InputStream getCredentialFileForOAuthClient(){
        return GoogleOAuthDemoUtil.class.getResourceAsStream(CRED_FILE_PATH);
    }

    public static void initRequestCaching(final String key, final GoogleAPIActionResponse spr){
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                serviceProviderRequestCache.put(generateHash(key), spr);
                return null;
            }
        });
    }

    public static void initTokenCaching(final String key, final AbstractOAuthToken value){
        executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                oauthTokenCache.put(key, value);
                return null;
            }
        });
    }

    public static AbstractOAuthToken getTokenFromTokenCache(String key){
        return oauthTokenCache.get(key);
    }

    public static boolean isTokenCached(String key){
        return oauthTokenCache.containsKey(key);
    }

    private static String generateHash(String input) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte [] byteArrayEq = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder("");

        for(byte b : byteArrayEq){
            stringBuilder.append(String.format("%02X", b));
        }

        return stringBuilder.toString();
    }

    public static boolean isSPRCached(String key) throws NoSuchAlgorithmException{
        return serviceProviderRequestCache.containsKey(generateHash(key));
    }

    public static GoogleAPIActionResponse getSPRFromCache(String key) throws NoSuchAlgorithmException{
        return serviceProviderRequestCache.get(generateHash(key));
    }
}
