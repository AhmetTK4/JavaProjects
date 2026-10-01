package com.atk.proxydesignpattern.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "email_change_audit")
public class EmailChangeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant occurredAt;

    /** Username from the caller context, or {@code null} when no caller was given. */
    private String actor;

    @Column(nullable = false)
    private Long targetUserId;

    private String oldEmail;

    private String newEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailChangeOutcome outcome;

    private String reason;

    public EmailChangeAudit(Instant occurredAt, String actor, Long targetUserId, String oldEmail,
                            String newEmail, EmailChangeOutcome outcome, String reason) {
        this.occurredAt = occurredAt;
        this.actor = actor;
        this.targetUserId = targetUserId;
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
        this.outcome = outcome;
        this.reason = reason;
    }
}
