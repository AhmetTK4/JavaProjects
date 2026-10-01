package com.atk.proxydesignpattern.audit;

import com.atk.proxydesignpattern.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmailChangeAuditTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailChangeAuditRepository auditRepository;

    private Long id(String username) {
        return userRepository.findByUsername(username).orElseThrow().getId();
    }

    private EmailChangeAudit latest() {
        List<EmailChangeAudit> all = auditRepository.findAllByOrderByOccurredAtDescIdDesc();
        assertFalse(all.isEmpty());
        return all.getFirst();
    }

    @Test
    void successfulChangeIsRecordedOnceWithOldAndNewEmail() throws Exception {
        long before = auditRepository.count();
        Long regular = id("regularUser");

        mvc.perform(put("/api/users/{id}/email", regular).header("X-User", "adminUser")
                        .param("newEmail", "audited@example.com"))
                .andExpect(status().isOk());

        assertEquals(before + 1, auditRepository.count(), "one record per request, not one per proxy");
        EmailChangeAudit audit = latest();
        assertEquals(EmailChangeOutcome.SUCCESS, audit.getOutcome());
        assertEquals("adminUser", audit.getActor());
        assertEquals(regular, audit.getTargetUserId());
        assertEquals("user@example.com", audit.getOldEmail());
        assertEquals("audited@example.com", audit.getNewEmail());
    }

    @Test
    void rejectedAttemptsAreRecordedAsDenied() throws Exception {
        Long admin = id("adminUser");

        mvc.perform(put("/api/users/{id}/email", admin).header("X-User", "regularUser")
                        .param("newEmail", "hijack@example.com"))
                .andExpect(status().isForbidden());
        EmailChangeAudit denied = latest();
        assertEquals(EmailChangeOutcome.DENIED, denied.getOutcome());
        assertEquals("regularUser", denied.getActor());
        assertEquals("admin@example.com", denied.getOldEmail());
        assertNotNull(denied.getReason());

        mvc.perform(put("/api/users/{id}/email", admin).param("newEmail", "anon@example.com"))
                .andExpect(status().isUnauthorized());
        EmailChangeAudit anonymous = latest();
        assertEquals(EmailChangeOutcome.DENIED, anonymous.getOutcome());
        assertNull(anonymous.getActor());
    }

    @Test
    void allowedButFailedChangeIsRecordedAsFailed() throws Exception {
        mvc.perform(put("/api/users/{id}/email", 9999).header("X-User", "adminUser")
                        .param("newEmail", "nobody@example.com"))
                .andExpect(status().isNotFound());
        EmailChangeAudit failed = latest();
        assertEquals(EmailChangeOutcome.FAILED, failed.getOutcome());
        assertEquals(9999L, failed.getTargetUserId());
        assertNull(failed.getOldEmail());
    }

    @Test
    void onlyAdminsCanReadTheAuditLog() throws Exception {
        mvc.perform(put("/api/users/{id}/email", id("regularUser")).header("X-User", "adminUser")
                .param("newEmail", "first@example.com"));
        mvc.perform(put("/api/users/{id}/email", id("regularUser")).header("X-User", "adminUser")
                .param("newEmail", "second@example.com"));

        mvc.perform(get("/api/audit/email-changes").header("X-User", "adminUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].newEmail").value("second@example.com"))
                .andExpect(jsonPath("$[1].newEmail").value("first@example.com"));
        mvc.perform(get("/api/audit/email-changes").header("X-User", "regularUser"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/audit/email-changes"))
                .andExpect(status().isUnauthorized());
    }
}
