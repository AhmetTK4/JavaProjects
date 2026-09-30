package com.atk.proxydesignpattern.service;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.AuthenticationRequiredException;
import com.atk.proxydesignpattern.exception.ForbiddenOperationException;
import com.atk.proxydesignpattern.exception.UserNotFoundException;
import com.atk.proxydesignpattern.security.CallerContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Protection proxy: same interface as the real service, but checks the role of the
 * <em>caller</em> before delegating write operations. Clients inject {@link UserService}
 * and receive this proxy because it is {@link Primary}.
 * <p>
 * Its target is the {@link CachingUserServiceProxy}, which in turn wraps {@link UserServiceImpl}.
 */
@Service
@Primary
public class UserServiceProxy implements UserService {
    private final UserService target;
    private final CallerContext callerContext;

    public UserServiceProxy(@Qualifier("cachingUserServiceProxy") UserService target, CallerContext callerContext) {
        this.target = target;
        this.callerContext = callerContext;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return target.getUserById(id);
    }

    @Override
    public void updateUserEmail(Long id, String newEmail) {
        User caller = currentCaller();
        if (!isAdmin(caller)) {
            throw new ForbiddenOperationException("Only admins can update email addresses.");
        }
        target.updateUserEmail(id, newEmail);
    }

    @Override
    public User findByUsername(String username) {
        return target.findByUsername(username);
    }

    private User currentCaller() {
        String username = callerContext.currentUsername()
                .orElseThrow(() -> new AuthenticationRequiredException("Caller identity is required."));
        try {
            return target.findByUsername(username);
        } catch (UserNotFoundException e) {
            throw new AuthenticationRequiredException("Unknown caller.");
        }
    }

    private boolean isAdmin(User user) {
        return "admin".equalsIgnoreCase(user.getRole());
    }
}
