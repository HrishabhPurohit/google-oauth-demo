package com.hporg.demo.rest.resource;

import java.util.Arrays;

/**
 * @author hrishabh.purohit
 * POJO representing the allowed scope for a given Google API
 */
public class OAuthScope {
    private String scopeServiceName;
    private String scopeLabel;
    private String description;
    private String [] allowedActions;
    
    public String getDescription() {
        return description;
    }
    public String[] getAllowedActions() {
        return allowedActions;
    }
    public void setAllowedActions(String[] allowedActions) {
        this.allowedActions = allowedActions;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getScopeServiceName() {
        return scopeServiceName;
    }
    public void setScopeServiceName(String scopeServiceName) {
        this.scopeServiceName = scopeServiceName;
    }
    public String getScopeLabel() {
        return scopeLabel;
    }
    public void setScopeLabel(String scopeLabel) {
        this.scopeLabel = scopeLabel;
    }

    @Override
    public String toString() {
        return "Scope Service Name : " + scopeServiceName + ", Scope Label Name : " + scopeLabel + ", Description : " + description + ", Allowed Actions : " + Arrays.toString(allowedActions);
    }
}
