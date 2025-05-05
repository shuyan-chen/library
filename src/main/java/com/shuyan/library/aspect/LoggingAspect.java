package com.shuyan.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(public * com.shuyan.library.service.*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        String classAndMethod = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();
        System.out.println("[LOG] call: " + classAndMethod + " param: " + Arrays.toString(args));

        Object result = pjp.proceed();

        System.out.println("[LOG] return: " + classAndMethod + " result: " + result);
        return result;
    }
}
