package com.hporg.demo.utils;

public enum EServiceProviderAPIs {
    GMAIL("Gmail API");

    private EServiceProviderAPIs(String apiName) {
        this.apiName = apiName;
    }
    private String apiName;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

}
