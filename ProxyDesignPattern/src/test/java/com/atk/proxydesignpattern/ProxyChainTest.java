package com.atk.proxydesignpattern;

import com.atk.proxydesignpattern.repository.UserRepository;
import com.atk.proxydesignpattern.service.CachingUserServiceProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Requests go through protection proxy -> caching proxy -> real subject. */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
class ProxyChainTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CachingUserServiceProxy cachingProxy;

    @Test
    void readsAreCachedAndUpdatesAreVisibleImmediately() throws Exception {
        Long id = userRepository.findByUsername("regularUser").orElseThrow().getId();
        long hitsBefore = cachingProxy.stats().hits();

        mvc.perform(get("/api/users/{id}", id)).andExpect(status().isOk());
        mvc.perform(get("/api/users/{id}", id)).andExpect(status().isOk());
        mvc.perform(get("/api/users/cache/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hits").value(hitsBefore + 1));

        // The write passes the protection proxy, then the caching proxy invalidates the entry.
        mvc.perform(put("/api/users/{id}/email", id).header("X-User", "adminUser")
                        .param("newEmail", "fresh@example.com"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/users/{id}", id))
                .andExpect(jsonPath("$.email").value("fresh@example.com"));
    }
}
