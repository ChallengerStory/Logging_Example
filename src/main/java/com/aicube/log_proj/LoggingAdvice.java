package com.aicube.log_proj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAdvice {
    @Around("@annotation(com.aicube.log_proj.Logging)")
    public Object atTarget(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature sig = (MethodSignature) jp.getSignature();
        String className = sig.getDeclaringType().getSimpleName();
        String method = sig.getName();
        log.info("[START] {}.{}", className, method);
        try {
            Object result = jp.proceed();
            log.info("[END] {}.{}", className, method);
            return result;
        } catch (Throwable e) {
            log.error("[ERROR] {}.{}.{}", className, method, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
