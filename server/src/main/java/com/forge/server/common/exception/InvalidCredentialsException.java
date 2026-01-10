package com.forge.server.common.exception;

/**
 * Exception thrown when authentication fails due to invalid credentials
 * (incorrect email or password).
 *
 * @author Forge Team
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}

