package com.atk.proxydesignpattern.security;

import java.util.Optional;

/**
 * Supplies the username of whoever is making the current call.
 * The protection proxy asks this instead of trusting data about the target user.
 */
public interface CallerContext {
    Optional<String> currentUsername();
}
