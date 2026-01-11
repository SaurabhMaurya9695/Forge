package com.forge.github;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

final class GithubApiClient {

    private static final String GITHUB_API_BASE = "https://api.github.com";
    private static final String GITHUB_API_USER_ENDPOINT = GITHUB_API_BASE + "/user";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String ACCEPT_HEADER_VALUE = "application/vnd.github.v3+json";
    private static final int CONNECT_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS = 10000;
    private static final String JSON_LOGIN_FIELD = "\"login\":\"";
    private static final String FAILED_UNAUTHORIZED_401 = "GitHub authentication failed: Unauthorized (401)";
    private static final String UNEXPECTED_RESPONSE_CODE = "GitHub API returned unexpected response code: ";
    private static final String ERROR_VERIFYING_GITHUB_CREDENTIALS = "Error verifying GitHub credentials: ";
    private static final String COULD_NOT_FIND_LOGIN_FIELD_IN_GITHUB_API_RESPONSE =
            "Could not find 'login' field in GitHub API response";
    private static final String INVALID_LOGIN_FIELD_FORMAT_IN_GITHUB_API_RESPONSE =
            "Invalid 'login' field format in GitHub API response";
    private static final String GET = "GET";

    private final Logger logger;

    GithubApiClient(Logger logger) {
        this.logger = logger;
    }

    String verifyCredentials(Credentials credentials) {
        try {
            HttpURLConnection connection = createConnection(credentials);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readResponse(connection);
                return extractUsername(response);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                logger.warning(FAILED_UNAUTHORIZED_401);
                return null;
            } else {
                logger.warning(UNEXPECTED_RESPONSE_CODE + responseCode);
                return null;
            }
        } catch (IOException e) {
            logger.warning(ERROR_VERIFYING_GITHUB_CREDENTIALS + e.getMessage());
            return null;
        }
    }

    private HttpURLConnection createConnection(Credentials credentials) throws IOException {
        URL url = new URL(GITHUB_API_USER_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(GET);
        connection.setRequestProperty(HEADER_ACCEPT, ACCEPT_HEADER_VALUE);
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);

        if (credentials.getType() == Credentials.Type.TOKEN) {
            connection.setRequestProperty(HEADER_AUTHORIZATION, "token " + credentials.getToken());
        } else {
            String authString = credentials.getUsername() + ":" + credentials.getPassword();
            String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty(HEADER_AUTHORIZATION, "Basic " + encodedAuth);
        }

        return connection;
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream(); BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private String extractUsername(String jsonResponse) {
        int loginStart = jsonResponse.indexOf(JSON_LOGIN_FIELD);
        if (loginStart == -1) {
            logger.warning(COULD_NOT_FIND_LOGIN_FIELD_IN_GITHUB_API_RESPONSE);
            return null;
        }

        loginStart += JSON_LOGIN_FIELD.length();
        int loginEnd = jsonResponse.indexOf("\"", loginStart);
        if (loginEnd == -1 || loginEnd <= loginStart) {
            logger.warning(INVALID_LOGIN_FIELD_FORMAT_IN_GITHUB_API_RESPONSE);
            return null;
        }

        return jsonResponse.substring(loginStart, loginEnd);
    }
}

