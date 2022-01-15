package com.hporg.demo.model;

/**
 * @author hrishabh.purohit
 * <p> POJO for representing Google API details
 */
public class SupportedGoogleAPIs {

    private String googleAPIServiceName;
    private String googleAPILabel;
    
    public String getGoogleAPIServiceName() {
        return googleAPIServiceName;
    }
    public void setGoogleAPIServiceName(String googleAPIServiceName) {
        this.googleAPIServiceName = googleAPIServiceName;
    }
    public String getGoogleAPILabel() {
        return googleAPILabel;
    }
    public void setGoogleAPILabel(String googleAPILabel) {
        this.googleAPILabel = googleAPILabel;
    }
}
