package com.dena.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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

    @Pointcut("execution(* com.dena.controller.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void log(JoinPoint joinPoint) {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        logger.info("url:{},ip->:{},method:{}", request.getRequestURI(), request.getRemoteAddr(), request.getMethod());

        Signature signature = joinPoint.getSignature();
        logger.info("类名：{},方法:{}", signature.getDeclaringTypeName(), signature.getName());
        logger.info("请求参数：｛｝", joinPoint.getArgs());
        logger.info("logger 1111111 l.....");
    }

    @AfterReturning(returning = "object", pointcut = "pointcut()")
    private void afterReturning(Object object) {
        logger.info("结果：{}", object);
    }
}
