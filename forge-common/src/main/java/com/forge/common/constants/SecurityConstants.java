package com.forge.common.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Security-related constants
 * <p>
 * Contains all constants related to security configuration, CORS, headers, and HTTP methods.
 */
public final class SecurityConstants {

    private SecurityConstants() {
        // Utility class - prevent instantiation
    }

    // HTTP Headers
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_TOTAL_COUNT = "X-Total-Count";

    // HTTP Methods
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_PATCH = "PATCH";

    // Allowed HTTP Methods
    public static final List<String> ALLOWED_METHODS = Arrays.asList(
            METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE, METHOD_OPTIONS, METHOD_PATCH
    );

    // Allowed Origins (CORS)
    public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:5173",
            "http://localhost:3000",
            "http://127.0.0.1:5173",
            "http://127.0.0.1:5174",
            "http://localhost:5174"
    );

    // Exposed Headers
    public static final List<String> EXPOSED_HEADERS = Arrays.asList(
            HEADER_AUTHORIZATION,
            HEADER_CONTENT_TYPE,
            HEADER_TOTAL_COUNT
    );

    // CORS Max Age (in seconds)
    public static final long CORS_MAX_AGE = 3600L;
}

