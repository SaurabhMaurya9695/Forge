package com.forge.server.common.exception;

/**
 * Exception thrown when attempting to register a user that already exists
 * (duplicate username or email).
 *
 * @author Forge Team
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

