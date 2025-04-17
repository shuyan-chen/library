package com.shuyan.library.aspect;

import com.shuyan.library.util.MethodKeyGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.shuyan.library.util.MethodKeyGenerator.generateKey;

@Component
@Aspect
public class LoggingAndCachingAspect {
    private Map<String, Object> cacheMap = new HashMap<>();

    @Around("execution(* com.shuyan.library.service..*(..))")
    public Object logAndCache(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        System.out.println("Calling method: " + methodName + " with args: " + Arrays.toString(args));

        Class<?> returnType = signature.getReturnType();
        boolean isCacheable = args != null&& args.length >0 && !returnType.equals(Void.TYPE);

        if(isCacheable){
            String key = generateKey(methodName,args);
            if(cacheMap.containsKey(key)){
                Object cachedResult = cacheMap.get(key);
                System.out.println("Returning cached result for method: " + methodName + " with key: " + key);
                return cachedResult;
            }
        }

        Object result = joinPoint.proceed();

        System.out.println("Method: " + methodName + " returned: " + result);

        if (isCacheable) {
            String key = generateKey(methodName, args);
            cacheMap.put(key, result);
            System.out.println("Caching result for method: " + methodName + " with key: " + key);
        }

        return result;
    }


}
