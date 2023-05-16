package com.common;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.common.LogAnnotation)")
    public void pointcut(){}
    @Before("pointcut()")
    public void methodBefore(JoinPoint point){
        LogAnnotation annotation = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(LogAnnotation.class);
        logger.info("开始" + annotation.title() + "---当前执行函数" + point.getTarget().getClass() + "$" + ((MethodSignature) point.getSignature()).getMethod().getName());
    }
    @After("pointcut()")
    public void methodAfter(JoinPoint point){
        LogAnnotation annotation = ((MethodSignature) point.getSignature()).getMethod().getAnnotation(LogAnnotation.class);
        logger.info(annotation.title() + "结束");
    }
    @AfterThrowing(throwing = "ex",pointcut = "pointcut()")
    public void exceptionInfo(JoinPoint joinPoint,Throwable ex){
        logger.error("-------------------------------------------------------------------------------------------------------------------------------------");
        logger.error("在" + joinPoint.getTarget().getClass() + "中" + joinPoint.getSignature().getName() + "方法发生异常，异常信息：{" + ex.getMessage() + "}");
        logger.error("参数值" + Arrays.toString(joinPoint.getArgs()));
        logger.error("-------------------------------------------------------------------------------------------------------------------------------------");
    }
}
