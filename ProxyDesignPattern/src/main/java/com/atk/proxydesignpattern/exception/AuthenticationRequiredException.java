package com.atk.proxydesignpattern.exception;

public class AuthenticationRequiredException extends RuntimeException {
    public AuthenticationRequiredException(String message) {
        super(message);
    }
}
