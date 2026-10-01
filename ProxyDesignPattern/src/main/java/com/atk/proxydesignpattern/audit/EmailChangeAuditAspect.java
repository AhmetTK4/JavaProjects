package com.atk.proxydesignpattern.audit;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.AuthenticationRequiredException;
import com.atk.proxydesignpattern.exception.ForbiddenOperationException;
import com.atk.proxydesignpattern.repository.UserRepository;
import com.atk.proxydesignpattern.security.CallerContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * Records every email change attempt. Spring applies this aspect through an AOP proxy around the
 * outermost {@code userServiceProxy} bean, so attempts the protection proxy rejects are recorded too,
 * and the call is audited once even though it travels through several proxies.
 */
@Aspect
@Component
public class EmailChangeAuditAspect {

    private final EmailChangeAuditRepository auditRepository;
    private final UserRepository userRepository;
    private final CallerContext callerContext;
    private final Clock clock;

    public EmailChangeAuditAspect(EmailChangeAuditRepository auditRepository, UserRepository userRepository,
                                  CallerContext callerContext) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
        this.callerContext = callerContext;
        this.clock = Clock.systemUTC();
    }

    @Around("execution(* com.atk.proxydesignpattern.service.UserService.updateUserEmail(..))"
            + " && bean(userServiceProxy) && args(id, newEmail)")
    public Object audit(ProceedingJoinPoint joinPoint, Long id, String newEmail) throws Throwable {
        String actor = callerContext.currentUsername().orElse(null);
        // Read the old value directly from the repository, bypassing any caching proxy.
        String oldEmail = userRepository.findById(id).map(User::getEmail).orElse(null);
        try {
            Object result = joinPoint.proceed();
            record(actor, id, oldEmail, newEmail, EmailChangeOutcome.SUCCESS, null);
            return result;
        } catch (AuthenticationRequiredException | ForbiddenOperationException e) {
            record(actor, id, oldEmail, newEmail, EmailChangeOutcome.DENIED, e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            record(actor, id, oldEmail, newEmail, EmailChangeOutcome.FAILED, e.getMessage());
            throw e;
        }
    }

    private void record(String actor, Long id, String oldEmail, String newEmail,
                        EmailChangeOutcome outcome, String reason) {
        auditRepository.save(new EmailChangeAudit(clock.instant(), actor, id, oldEmail, newEmail, outcome, reason));
    }
}
