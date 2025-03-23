package com.pwc.security.aspect;

import com.pwc.security.annotation.CacheException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(2)
public class RedisCacheAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Pointcut("execution(public * com.pwc.service.*CacheService.*(..)) " +
            "|| execution(public * com.pwc.*.service.*CacheService.*(..))")
    public void pointcut() {}
   
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object result = null;
        try{
            result = joinPoint.proceed();
        }catch (Throwable e){
            LOGGER.error(e.getMessage());
            if (method.isAnnotationPresent(CacheException.class)) {
                throw e;
            }
        }
        return result;
    }
}
