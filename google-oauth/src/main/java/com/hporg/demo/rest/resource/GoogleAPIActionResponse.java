package com.hporg.demo.rest.resource;

import org.springframework.http.HttpStatus;

/**
 * @author hrishabh.purohit
 */
public class GoogleAPIActionResponse {
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String result;

    private HttpStatus statusCode;

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
