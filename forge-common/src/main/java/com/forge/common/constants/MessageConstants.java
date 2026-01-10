package com.forge.common.constants;

/**
 * Message constants
 * <p>
 * Contains all user-facing messages, success messages, and error messages.
 */
public final class MessageConstants {

    private MessageConstants() {
        // Utility class - prevent instantiation
    }

    // Success Messages
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";

    // Error Messages
    public static final String ERROR_INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    public static final String ERROR_VALIDATION_FAILED = "Validation failed";
    public static final String ERROR_UNEXPECTED = "An unexpected error occurred: %s";

    // Status Messages
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_UP = "UP";
}

