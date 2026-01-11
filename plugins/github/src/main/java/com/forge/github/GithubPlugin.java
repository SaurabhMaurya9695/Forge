package com.forge.github;

import com.forge.server.plugins.api.Plugin;
import com.forge.server.plugins.api.PluginContext;
import com.forge.server.plugins.api.PluginState;
import com.forge.server.common.plugin.exception.PluginException;

import java.util.logging.Logger;

public class GithubPlugin implements Plugin {

    private static final String PLUGIN_NAME = "github-plugin";
    private static final String PLUGIN_VERSION = "1.0.0";

    private static final String CONFIG_TOKEN = "github.token";
    private static final String CONFIG_USERNAME = "github.username";
    private static final String CONFIG_PASSWORD = "github.password";
    private static final String INITIALIZING_GITHUB_PLUGIN = "Initializing GitHub plugin";
    private static final String GITHUB_PLUGIN_INITIALIZED_SUCCESSFULLY = "GitHub plugin initialized successfully";
    private static final String STARTING_GITHUB_PLUGIN = "Starting GitHub plugin";
    private static final String PLUGIN_WILL_OPERATE_IN_LIMITED_MODE =
            "No GitHub credentials provided. Plugin will operate in limited mode.";
    private static final String FAILED_INVALID_OR_EXPIRED_CREDENTIALS =
            "GitHub credential verification failed. Invalid or expired credentials.";
    private static final String VERIFIED_SUCCESSFULLY_AUTHENTICATED_AS =
            "GitHub credentials verified successfully. Authenticated as: ";
    private static final String PLUGIN_STARTED_AND_READY_FOR_OPERATIONS =
            "GitHub plugin started and ready for operations";
    private static final String STOPPING_GITHUB_PLUGIN = "Stopping GitHub plugin";

    private PluginState state = PluginState.LOADED;
    private PluginContext context;
    private Logger logger;
    private GithubApiClient apiClient;
    private String authenticatedUsername;

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public void init(PluginContext pluginContext) throws PluginException {
        logger.info(INITIALIZING_GITHUB_PLUGIN);
        this.context = pluginContext;
        this.logger = context.getLogger();
        this.apiClient = new GithubApiClient(logger);
        state = PluginState.INITIALIZED;
        logger.info(GITHUB_PLUGIN_INITIALIZED_SUCCESSFULLY);
    }

    @Override
    public void start() throws PluginException {
        logger.info(STARTING_GITHUB_PLUGIN);

        Credentials credentials = loadCredentials();
        if (credentials == null) {
            logger.warning(PLUGIN_WILL_OPERATE_IN_LIMITED_MODE);
            logger.warning(
                    "To enable full functionality, configure either '" + CONFIG_TOKEN + "' or '" + CONFIG_USERNAME
                            + "' and '" + CONFIG_PASSWORD + "'");
            state = PluginState.STARTED;
            return;
        }

        authenticatedUsername = apiClient.verifyCredentials(credentials);
        if (authenticatedUsername == null) {
            throw new PluginException(FAILED_INVALID_OR_EXPIRED_CREDENTIALS);
        }

        logger.info(VERIFIED_SUCCESSFULLY_AUTHENTICATED_AS + authenticatedUsername);
        state = PluginState.STARTED;
        logger.info(PLUGIN_STARTED_AND_READY_FOR_OPERATIONS);
    }

    @Override
    public void stop() throws PluginException {
        logger.info(STOPPING_GITHUB_PLUGIN);
        state = PluginState.STOPPED;
        logger.info("GitHub plugin stopped");
    }

    @Override
    public void destroy() {
        if (logger != null) {
            logger.info("Destroying GitHub plugin");
        }
        authenticatedUsername = null;
        apiClient = null;
        state = PluginState.UNLOADED;
    }

    @Override
    public PluginState getState() {
        return state;
    }

    public String getAuthenticatedUsername() {
        return authenticatedUsername;
    }

    public boolean isAuthenticated() {
        return authenticatedUsername != null && state == PluginState.STARTED;
    }

    private Credentials loadCredentials() {
        String token = context.getConfig(CONFIG_TOKEN).orElse(null);
        if (token != null && !token.trim().isEmpty()) {
            return Credentials.token(token.trim());
        }

        String username = context.getConfig(CONFIG_USERNAME).orElse(null);
        String password = context.getConfig(CONFIG_PASSWORD).orElse(null);
        if (username != null && password != null && !username.trim().isEmpty() && !password.trim().isEmpty()) {
            return Credentials.usernamePassword(username.trim(), password.trim());
        }

        return null;
    }
}
