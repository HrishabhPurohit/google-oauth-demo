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
 * Static utility class for google oauth demo. Primary responsibilities include:
 * <p> 1. Caching serviceProvider implementation for a given domain.
 * <p> 2. Caching serviceProviderAPI implementation for a given api name.
 * <p> 3. Caching serviceProviderAPIClient implementation for a given api name.
 * <p> 4. Asynchronous caching of serviceProviderRequest for a given set of user inputs.
 * <p> 5. Asynchronous caching of oauthToken implementation for a given user name.
 */
public class GoogleOAuthDemoUtil {
    
    private static final Logger LOGGER = LogManager.getLogger(GoogleOAuthDemoUtil.class);
    private static final Map<String, AbstractServiceProvider> DOMAIN_VS_SERVICE_PROVIDER_CACHE;
    private static final Map<String, AbstractServiceProviderAPI> API_NAME_VS_SERVICE_PROVIDER_API_CACHE;
    private static final Map<String, IServiceProviderAPIClient<?>> API_NAME_VS_SERVICE_PROVIDER_API_CLIENT;
    private static final ExecutorService EXECUTOR_SERVICE;
    private static final Map<String, GoogleAPIActionResponse> SERVICE_PROVIDER_REQUEST_CACHE;
    private static final Map<String, AbstractOAuthToken> OAUTH_TOKEN_CACHE;

    @Autowired
    @Value("${google.oauth.approach.credfile.path}")
    private static String CRED_FILE_PATH;

    static{
        LOGGER.debug("Initializing domain vs service provider cache");

        DOMAIN_VS_SERVICE_PROVIDER_CACHE = new HashMap<String, AbstractServiceProvider>();
        DOMAIN_VS_SERVICE_PROVIDER_CACHE.put(EServiceProviders.GMAIL.getDomainName(), new GoogleServiceProvider(EServiceProviders.GMAIL.getDomainName()));

        LOGGER.debug("Successfully initialized domain vs service provider cache");
        LOGGER.debug("Initializing name vs service provider api cache");

        API_NAME_VS_SERVICE_PROVIDER_API_CACHE = new HashMap<String, AbstractServiceProviderAPI>();
        API_NAME_VS_SERVICE_PROVIDER_API_CACHE.put(EServiceProviderAPIs.GMAIL.getApiName(), new GmailServiceProviderAPI(EServiceProviderAPIs.GMAIL.getApiName()));

        LOGGER.debug("Successfully initialized name vs service provider api cache");
        LOGGER.debug("Initializing name vs service provider api client cache");

        API_NAME_VS_SERVICE_PROVIDER_API_CLIENT = new HashMap<String, IServiceProviderAPIClient<?>>();
        API_NAME_VS_SERVICE_PROVIDER_API_CLIENT.put(EServiceProviderAPIs.GMAIL.getApiName(), new GmailAPIClient());

        LOGGER.debug("Initializing service provider request cache map");
        SERVICE_PROVIDER_REQUEST_CACHE = new ConcurrentHashMap<String, GoogleAPIActionResponse>();
        LOGGER.debug("Successfully initialized service provider request cache map");

        LOGGER.debug("Initializing oauth token cache map");
        OAUTH_TOKEN_CACHE = new ConcurrentHashMap<String, AbstractOAuthToken>();
        LOGGER.debug("Successfully initialized oauth token cache map");

        LOGGER.debug("Initializing threadpool for executor service");
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
        LOGGER.debug("Successfully initialized threadpool of 2 threads for executor service");
    }
    
    public static AbstractServiceProvider resolveServiceProviderFromUserName(String userName){
        String domainName = null;

        if(userName != null && !userName.equals("") && userName.indexOf("@") != -1){
            domainName = userName.substring(userName.indexOf("@") + 1);
        }
        
        return DOMAIN_VS_SERVICE_PROVIDER_CACHE.get(domainName);
    }

    public static AbstractServiceProviderAPI resolveServiceProviderAPIFromAPIName(String apiName){
        return API_NAME_VS_SERVICE_PROVIDER_API_CACHE.get(apiName);
    }

    public static IServiceProviderAPIClient<?> resolveAPIClientFromAPIName(String apiName){
        return API_NAME_VS_SERVICE_PROVIDER_API_CLIENT.get(apiName);
    }

    public static InputStream getCredentialFileForOAuthClient(){
        return GoogleOAuthDemoUtil.class.getResourceAsStream(CRED_FILE_PATH);
    }

    public static void initRequestCaching(final String key, final GoogleAPIActionResponse spr){
        EXECUTOR_SERVICE.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SERVICE_PROVIDER_REQUEST_CACHE.put(generateHash(key), spr);
                return null;
            }
        });
    }

    public static void initTokenCaching(final String key, final AbstractOAuthToken value){
        if(!OAUTH_TOKEN_CACHE.containsKey(key)){
            EXECUTOR_SERVICE.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    OAUTH_TOKEN_CACHE.put(key, value);
                    return null;
                }
            });
        }
    }

    public static AbstractOAuthToken getTokenFromTokenCache(String key){
        return OAUTH_TOKEN_CACHE.get(key);
    }

    public static boolean isTokenCached(String key){
        return OAUTH_TOKEN_CACHE.containsKey(key);
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

    public static Boolean isSPRCached(String key){
        String hash;
        try{
            hash = generateHash(key);
        } catch (NoSuchAlgorithmException e){
            LOGGER.error("Exception occurred while generating hash string for key : " + key);
            return null;
        }

        return SERVICE_PROVIDER_REQUEST_CACHE.containsKey(hash);
    }

    public static GoogleAPIActionResponse getSPRFromCache(String key){
        String hash;
        try{
            hash = generateHash(key);
        } catch (NoSuchAlgorithmException e){
            LOGGER.error("Exception occurred while generating hash string for key : " + key);
            return null;
        }

        return SERVICE_PROVIDER_REQUEST_CACHE.get(hash);
    }
}
