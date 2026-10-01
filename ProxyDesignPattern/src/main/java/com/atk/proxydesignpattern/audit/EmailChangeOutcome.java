package com.atk.proxydesignpattern.audit;

public enum EmailChangeOutcome {
    /** The email was changed. */
    SUCCESS,
    /** The protection proxy rejected the caller (missing, unknown or not an admin). */
    DENIED,
    /** The caller was allowed, but the change failed (e.g. unknown user). */
    FAILED
}
