package com.aspect;

import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class HttpAspect {

    //spring boot 默认日志使用logback
    private final Logger logger = LoggerFactory.getLogger(HttpAspect.class);


    @Pointcut("@within(org.apache.ibatis.annotations.Mapper)")
    public void execute() {
    }

    @Pointcut("@within(com.aspect.IgnoreHintManager)")
    public void aa() {
    }

    @Pointcut("execution(* com..dao..*(..)))")
    public void dao() {
    }


    //    @Pointcut("execution(* com..mapper..*(..)) && !@within(com.aspect.IgnoreHintManager)")
//    @Pointcut("execution(* com..mapper..*(..))")
    @Pointcut("dao() && !aa()")
    public void mapperExecute() {

    }

    @Before("mapperExecute()")
    public void ignoreHintManagerInMapper(JoinPoint pjp) {
        Class<?> classz = pjp.getTarget().getClass();
        IgnoreHintManager ignoreHintManager = classz.getAnnotation(IgnoreHintManager.class);
        System.out.println(ignoreHintManager);
        logger.info("--->111");
    }

    @Around("dao() && aa()")
    public Object ignoreHintManager(ProceedingJoinPoint pjp) throws Throwable {
        Class<?> classz = pjp.getTarget().getClass();
        IgnoreHintManager ignoreHintManager = classz.getAnnotation(IgnoreHintManager.class);
        System.out.println(ignoreHintManager);
        logger.info("--->222");
        return pjp.proceed();
    }

}
