package com.mozza.springboilerplate.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Time Trace AOP just work on local profile
 */

@Aspect
@Component
@Profile("local")
public class TimeTraceAop {

    @Around(value = "execution(* com.mozza.springboilerplate.service..*(..)) || execution(* com.mozza.springboilerplate.repository..*(..)) || execution(* com.mozza.springboilerplate.controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());

        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
