package com.hporg.demo.utils;

/**
 * @author hrishabh.purohit
 */
public enum EServiceProviders {
    GMAIL("gmail.com","GOOGLE"), OUTLOOK("outlook.com","MICROSOFT");

    String domainName;
    String serviceProviderName;

    EServiceProviders(String domainName, String serviceProviderName){
        this.domainName = domainName;
        this.serviceProviderName = serviceProviderName;
    }

    public String getDomainName() {
        return domainName;
    }
    public String getServiceProviderName() {
        return serviceProviderName;
    }
}
