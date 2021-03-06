package com.zb.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CalculatorAspect {
    @Pointcut("execution(* com.zb.aop.CalculatorService.mul(..))")
    public void pointCut() {}

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("前置增强");
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        String name = joinPoint.getSignature().getName();
        System.out.println(target.getClass().getName() +
                ": Parameters of the " + name +
                " method: [" + args[0] + "," + args[1] + "]");
    }

    @After("pointCut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("后置增强");
        Object target = joinPoint.getTarget();
        String name = joinPoint.getSignature().getName();
        System.out.println(target.getClass().getName() + ": The " + name +
                " method ends.");
    }

    @AfterReturning(value = "execution(public int mul(int, int))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Integer result) {
        System.out.println("返回增强");
        Object target = joinPoint.getTarget();
        String name = joinPoint.getSignature().getName();
        System.out.println(target.getClass().getName() + ": Result of the " +
                name + " method: " + result);
    }
}
