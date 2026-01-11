package com.forge.common.constants;

/**
 * JWT-related constants
 * <p>
 * Contains all constants related to JWT token generation, validation, and configuration.
 */
public final class JwtConstants {

    private JwtConstants() {
        // Utility class - prevent instantiation
    }

    // JWT Token Prefix
    public static final String TOKEN_PREFIX = "Bearer ";

    // JWT Header Name
    public static final String HEADER_NAME = "Authorization";

    // JWT Token Type
    public static final String TOKEN_TYPE = "Bearer";

    // JWT Claim Keys
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_NAME = "name";

    // JWT Error Messages
    public static final String ERROR_SECRET_NULL_OR_EMPTY =
            "JWT secret cannot be null or empty. Please configure app.jwt.secret in application.yml";
    public static final String ERROR_SECRET_TOO_SHORT =
            "JWT secret must be at least 32 bytes (256 bits) for HS256 algorithm. Current secret length: %d bytes. "
                    + "Please set a longer secret in app.jwt.secret configuration.";
    public static final String ERROR_SECRET_KEY_CREATION_FAILED =
            "Failed to create JWT secret key. Please ensure app.jwt.secret is properly configured.";
    public static final String ERROR_SECRET_INITIALIZATION_FAILED = "Failed to initialize JWT secret key";

    // JWT Log Messages
    public static final String LOG_INVALID_SIGNATURE = "Invalid JWT signature: {}";
    public static final String LOG_INVALID_TOKEN = "Invalid JWT token: {}";
    public static final String LOG_EXPIRED_TOKEN = "Expired JWT token: {}";
    public static final String LOG_UNSUPPORTED_TOKEN = "Unsupported JWT token: {}";
    public static final String LOG_EMPTY_CLAIMS = "JWT claims string is empty: {}";
    public static final String LOG_SECRET_HASHED =
            "JWT secret was shorter than 32 bytes. It has been hashed to meet the requirement.";
    public static final String LOG_SECRET_HASH_FAILED = "Failed to hash JWT secret: {}";
    public static final String LOG_SECRET_KEY_CREATION_FAILED = "Failed to create JWT secret key: {}";
    public static final String LOG_AUTHENTICATION_FAILED = "Failed to set user authentication: {}";
}

