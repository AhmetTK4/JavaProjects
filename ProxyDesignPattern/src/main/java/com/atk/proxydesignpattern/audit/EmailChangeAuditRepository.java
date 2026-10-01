package com.atk.proxydesignpattern.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailChangeAuditRepository extends JpaRepository<EmailChangeAudit, Long> {
    List<EmailChangeAudit> findAllByOrderByOccurredAtDescIdDesc();
}
