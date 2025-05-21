package com.shuyan.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Pointcut("execution(public * com.shuyan.library.service.*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        String classAndMethod = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();
        logger.info("call: {}, param: {}" ,classAndMethod,Arrays.toString(args));
        Object result = pjp.proceed();
        logger.info("return: {}, result: {}" , classAndMethod, result);
        return result;
    }
}
