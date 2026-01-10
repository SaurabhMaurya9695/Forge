package com.forge.common.constants;

/**
 * API-related constants
 * <p>
 * Contains all constants related to API endpoints, paths, and route mappings.
 */
public final class ApiConstants {

    private ApiConstants() {
        // Utility class - prevent instantiation
    }

    // API Base Paths
    public static final String API_BASE_PATH = "/api";
    public static final String API_AUTH_PATH = "/api/auth";
    public static final String API_HEALTH_PATH = "/api/health";

    // Authentication Endpoints
    public static final String ENDPOINT_REGISTER = "/register";
    public static final String ENDPOINT_LOGIN = "/login";

    // Health Check Endpoints
    public static final String ENDPOINT_HEALTH = "/";

    // Actuator Path
    public static final String ACTUATOR_PATH = "/actuator/**";

    // Full Endpoint Paths
    public static final String FULL_REGISTER_PATH = API_AUTH_PATH + ENDPOINT_REGISTER;
    public static final String FULL_LOGIN_PATH = API_AUTH_PATH + ENDPOINT_LOGIN;
    public static final String FULL_HEALTH_PATH = API_HEALTH_PATH + ENDPOINT_HEALTH;
}

