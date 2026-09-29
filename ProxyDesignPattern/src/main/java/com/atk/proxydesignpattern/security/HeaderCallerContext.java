package com.atk.proxydesignpattern.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Learning simplification: the caller is identified by the {@code X-User} header.
 * A real application would read the authenticated principal (e.g. Spring Security) instead.
 */
@Component
public class HeaderCallerContext implements CallerContext {

    public static final String HEADER = "X-User";

    @Override
    public Optional<String> currentUsername() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return Optional.empty();
        }
        return Optional.ofNullable(attributes.getRequest().getHeader(HEADER))
                .map(String::trim)
                .filter(name -> !name.isEmpty());
    }
}
