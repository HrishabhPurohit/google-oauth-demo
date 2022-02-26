package com.hporg.demo.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hrishabh.purohit
 * 
 * Use this annotation to retry a remote API method to be automatically retried (strictly once) in a scenario when the thrown Exception type matches that of the annotation onException attribute.
 * @see RetryableAspect
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Retryable {
    Class<?> onException();
}
