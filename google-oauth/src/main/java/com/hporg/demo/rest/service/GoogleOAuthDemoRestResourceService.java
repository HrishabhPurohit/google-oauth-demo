package com.hporg.demo.rest.service;

import java.util.List;

/**
 * @author hrishabh.purohit
 * @see GoogleAPIOperationResponseRestResource
 */
public interface GoogleOAuthDemoRestResourceService<T> {
    public List<T> get();
    public T post(String payload); // payload is expected to be a json string.
}
