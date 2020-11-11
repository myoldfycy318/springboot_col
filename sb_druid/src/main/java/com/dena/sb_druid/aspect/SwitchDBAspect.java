package com.dena.sb_druid.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(0)
@Component
@Slf4j
public class SwitchDBAspect {


    @Around("@annotation(switchDB)")
    public Object doAroundMethod(ProceedingJoinPoint pjp, SwitchDB switchDB) throws Throwable {
        String method = pjp.getSignature().getName();
        String simpleName = pjp.getSignature().getDeclaringType().getSimpleName();
        log.info("--0---->...->调用的方法名：{},调用的类名：{}", method, simpleName);
        return pjp.proceed();
    }


}
