package com.shuyan.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CacheAspect {
    private Map<String, Object> cache = new ConcurrentHashMap<>();

    @Around("execution(public * com.shuyan.library.service.*.*(..)) && !execution(void *(..))")
    public Object cache(ProceedingJoinPoint pjp) throws Throwable {
        String key = pjp.getSignature().toShortString() + Arrays.toString(pjp.getArgs());
        if (cache.containsKey(key)) {
            System.out.println("[CACHE]  " + key);
            return cache.get(key);
        }

        Object result = pjp.proceed();
        cache.put(key, result);
        System.out.println("[CACHE] cache: " + key);
        return result;
    }
}
