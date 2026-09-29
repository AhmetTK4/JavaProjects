package com.example.playwiththreads.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // nanoTime is monotonic; currentTimeMillis can jump when the wall clock changes.
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            log.info("{} metodu {} ms sürdü.", joinPoint.getSignature().toShortString(), elapsedMs);
        }
    }
}