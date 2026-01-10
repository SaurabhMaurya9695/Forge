package com.forge.common.constants;

/**
 * Password encoding-related constants
 * <p>
 * Contains all constants related to password encoding strategies.
 */
public final class PasswordConstants {

    private PasswordConstants() {
        // Utility class - prevent instantiation
    }

    // Password Encoding Strategies
    public static final String STRATEGY_BCRYPT = "BCRYPT";
    public static final String STRATEGY_PBKDF2 = "PBKDF2";

    // Role Prefix
    public static final String ROLE_PREFIX = "ROLE_";
}

