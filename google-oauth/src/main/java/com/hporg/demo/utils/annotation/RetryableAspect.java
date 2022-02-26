package com.hporg.demo.utils.annotation;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author hrishabh.purohit
 * 
 * AspectJ aspect to define an Around advice for the execution of {@link Retryable} methods. Retry count is set strictly to one.
 */
@Aspect
public class RetryableAspect {
    
    private static final Logger LOGGER = LogManager.getLogger(RetryableAspect.class);

    @Around(value = "@annotation(Retryable) && execution(* *(..))")
    public Object retry(ProceedingJoinPoint jPoint) throws Throwable{
        Object output = null;

        if(jPoint != null){
            MethodSignature methodSignature = (MethodSignature) jPoint.getSignature();
            Method method = methodSignature.getMethod();
            Retryable retryable = method.getAnnotation(Retryable.class);

            LOGGER.debug("Checking if method : " + methodSignature.getName() + " needs to be retried.");

            try{
                output = jPoint.proceed();
            } catch (Exception ex){
                if(ex.getClass() == retryable.onException()){
                    LOGGER.debug("Retrying method : " + methodSignature.getName() + " of class : " + jPoint.getTarget().getClass().toString());
                    output = jPoint.proceed();
                }
            }
        }
        
        return output;
    }
}
