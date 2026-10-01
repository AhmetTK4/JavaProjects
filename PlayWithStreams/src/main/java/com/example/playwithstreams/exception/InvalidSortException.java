package com.example.playwithstreams.exception;

import java.util.Set;

public class InvalidSortException extends RuntimeException {
    public InvalidSortException(String property, Set<String> allowed) {
        super("Cannot sort by '" + property + "'. Allowed: " + allowed.stream().sorted().toList());
    }
}
