package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.audit.EmailChangeAudit;
import com.atk.proxydesignpattern.audit.EmailChangeAuditRepository;
import com.atk.proxydesignpattern.exception.AuthenticationRequiredException;
import com.atk.proxydesignpattern.exception.ForbiddenOperationException;
import com.atk.proxydesignpattern.repository.UserRepository;
import com.atk.proxydesignpattern.security.CallerContext;
import com.atk.proxydesignpattern.security.HeaderCallerContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuditController {

    private final EmailChangeAuditRepository auditRepository;
    private final UserRepository userRepository;
    private final CallerContext callerContext;

    public AuditController(EmailChangeAuditRepository auditRepository, UserRepository userRepository,
                           CallerContext callerContext) {
        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
        this.callerContext = callerContext;
    }

    @Operation(summary = "E-posta değişikliği denemelerinin kaydı (yalnızca admin, en yeni önce)",
            parameters = @Parameter(in = ParameterIn.HEADER, name = HeaderCallerContext.HEADER, required = true))
    @GetMapping("/api/audit/email-changes")
    public List<EmailChangeAudit> emailChanges() {
        String username = callerContext.currentUsername()
                .orElseThrow(() -> new AuthenticationRequiredException("Caller identity is required."));
        String role = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationRequiredException("Unknown caller."))
                .getRole();
        if (!"admin".equalsIgnoreCase(role)) {
            throw new ForbiddenOperationException("Only admins can read the audit log.");
        }
        return auditRepository.findAllByOrderByOccurredAtDescIdDesc();
    }
}
