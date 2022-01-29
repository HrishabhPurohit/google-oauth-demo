package com.hporg.demo.rest.service;

import java.util.List;

public interface GoogleOAuthDemoRestResourceService<T> {
    public List<T> get();
    public T post(String payload); // payload is expected to be a json string.
}
