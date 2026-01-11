package com.forge.common.constants;

/**
 * Validation-related constants
 * <p>
 * Contains all constants related to validation messages and validation rules.
 */
public final class ValidationConstants {

    private ValidationConstants() {
        // Utility class - prevent instantiation
    }

    // Validation Error Messages
    public static final String VALIDATION_USERNAME_REQUIRED = "Username is required";
    public static final String VALIDATION_EMAIL_REQUIRED = "Email is required";
    public static final String VALIDATION_EMAIL_INVALID = "Email should be valid";
    public static final String VALIDATION_PASSWORD_REQUIRED = "Password is required";
    public static final String VALIDATION_PASSWORD_LENGTH_REQUIRED = "Password must be at least 8 characters long";
    public static final String VALIDATION_USERNAME_LENGTH_REQUIRED = "Username must be between 3 and 50 characters";
    
    // Plugin Validation Messages
    public static final String VALIDATION_PLUGIN_NAME_REQUIRED = "Plugin name is required";
    public static final String VALIDATION_JAR_PATH_REQUIRED = "JAR path is required";
    public static final String VALIDATION_CLASS_NAME_REQUIRED = "Class name is required";
}

