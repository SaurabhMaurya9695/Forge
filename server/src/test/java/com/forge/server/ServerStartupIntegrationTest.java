package com.forge.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests to verify Jetty server startup and health endpoints
 * <p>
 * These tests verify:
 * 1. Jetty server is running (not Tomcat)
 * 2. Actuator health endpoint is accessible
 * 3. Custom health endpoint is accessible and returns correct status
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ServerStartupIntegrationTest {

    // Endpoint paths
    private static final String ACTUATOR_HEALTH_PATH = "/actuator/health";
    private static final String CUSTOM_HEALTH_PATH = "/api/health";

    // Health status values
    private static final String STATUS_UP = "UP";
    private static final String SERVICE_NAME = "forge-server";

    // Response field names
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_SERVICE = "service";
    private static final String FIELD_TIMESTAMP = "timestamp";

    // Error messages
    private static final String ERROR_WEB_SERVER_FACTORY_NULL = "Web server factory should not be null";
    private static final String ERROR_SERVER_NOT_JETTY = "Server should be Jetty, but found: %s";
    private static final String ERROR_PORT_NOT_ASSIGNED = "Server port should be assigned";
    private static final String ERROR_ACTUATOR_HEALTH_NOT_200 = "Actuator health endpoint should return 200 OK";
    private static final String ERROR_CUSTOM_HEALTH_NOT_200 = "Custom health endpoint should return 200 OK";
    private static final String ERROR_SERVER_NOT_RESPONDING = "Server should respond with 200 OK";
    private static final String ERROR_HEALTH_BODY_NULL = "Health response body should not be null";
    private static final String ERROR_STATUS_FIELD_MISSING = "Health response should contain 'status' field";
    private static final String ERROR_STATUS_NOT_UP = "Health status should be UP";
    private static final String ERROR_RESPONSE_BODY_NULL = "Response body should not be null";
    private static final String ERROR_RESPONSE_BODY_EMPTY = "Response body should not be empty";
    private static final String ERROR_STATUS_FIELD_MISSING_CUSTOM = "Response should contain 'status' field";
    private static final String ERROR_SERVICE_FIELD_MISSING = "Response should contain 'service' field";
    private static final String ERROR_TIMESTAMP_FIELD_MISSING = "Response should contain 'timestamp' field";
    private static final String ERROR_STATUS_VALUE = "Status should be UP";
    private static final String ERROR_SERVICE_VALUE = "Service name should be forge-server";
    private static final String ERROR_TIMESTAMP_NULL = "Timestamp should not be null";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Test to verify that Jetty server is running instead of Tomcat
     */
    @Test
    void testJettyServerIsRunning() {
        // Check if ServletWebServerFactory bean exists
        ServletWebServerFactory webServerFactory = applicationContext.getBean(ServletWebServerFactory.class);

        // Verify it's a Jetty server factory, not Tomcat
        assertNotNull(webServerFactory, ERROR_WEB_SERVER_FACTORY_NULL);
        assertInstanceOf(JettyServletWebServerFactory.class, webServerFactory,
                String.format(ERROR_SERVER_NOT_JETTY, webServerFactory.getClass().getName()));

        // Verify the server is actually running by checking the port
        assertTrue(port > 0, ERROR_PORT_NOT_ASSIGNED);
    }

    /**
     * Test to verify Actuator health endpoint is accessible and returns UP status
     */
    @Test
    void testActuatorHealthEndpoint() {
        String url = "http://localhost:" + port + ACTUATOR_HEALTH_PATH;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        // Verify response status
        assertEquals(HttpStatus.OK, response.getStatusCode(), ERROR_ACTUATOR_HEALTH_NOT_200);

        // Verify response body contains status
        assertNotNull(response.getBody(), ERROR_HEALTH_BODY_NULL);
        assertTrue(response.getBody().containsKey(FIELD_STATUS), ERROR_STATUS_FIELD_MISSING);
        String status = (String) response.getBody().get(FIELD_STATUS);
        assertEquals(STATUS_UP, status, ERROR_STATUS_NOT_UP);
    }

    /**
     * Test to verify custom health endpoint is accessible and returns correct data
     */
    @Test
    void testCustomHealthEndpoint() {
        String url = "http://localhost:" + port + CUSTOM_HEALTH_PATH;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        // Verify response status
        assertEquals(HttpStatus.OK, response.getStatusCode(), ERROR_CUSTOM_HEALTH_NOT_200);

        // Verify response body
        assertNotNull(response.getBody(), ERROR_HEALTH_BODY_NULL);

        Map body = response.getBody();

        // Verify required fields
        assertTrue(body.containsKey(FIELD_STATUS), ERROR_STATUS_FIELD_MISSING_CUSTOM);
        assertTrue(body.containsKey(FIELD_SERVICE), ERROR_SERVICE_FIELD_MISSING);
        assertTrue(body.containsKey(FIELD_TIMESTAMP), ERROR_TIMESTAMP_FIELD_MISSING);

        // Verify field values
        assertEquals(STATUS_UP, body.get(FIELD_STATUS), ERROR_STATUS_VALUE);
        assertEquals(SERVICE_NAME, body.get(FIELD_SERVICE), ERROR_SERVICE_VALUE);
        assertNotNull(body.get(FIELD_TIMESTAMP), ERROR_TIMESTAMP_NULL);
    }

    /**
     * Test to verify server is responding to HTTP requests
     */
    @Test
    void testServerIsResponding() {
        String url = "http://localhost:" + port + CUSTOM_HEALTH_PATH;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Verify server is responding
        assertEquals(HttpStatus.OK, response.getStatusCode(), ERROR_SERVER_NOT_RESPONDING);
        assertNotNull(response.getBody(), ERROR_RESPONSE_BODY_NULL);
        assertFalse(response.getBody().isEmpty(), ERROR_RESPONSE_BODY_EMPTY);
    }
}

