package com.forge.github;

import com.forge.server.plugins.api.Plugin;
import com.forge.server.plugins.api.PluginContext;
import com.forge.server.plugins.api.PluginState;
import com.forge.server.common.plugin.exception.PluginException;

import java.util.logging.Logger;

public class GithubPlugin implements Plugin {

    private static final String PLUGIN_NAME = "github-plugin";
    private static final String PLUGIN_VERSION = "1.0.0";

    private PluginState state = PluginState.LOADED;
    private PluginContext context;
    private Logger logger;

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
        this.context = pluginContext;
        this.logger = context.getLogger();
        logger.info("Initializing GitHub plugin");

        String apiToken = context.getConfig("github.token").orElse("demo-token");
        logger.info("GitHub API token configured: " + (apiToken.equals("demo-token") ? "using demo token"
                : "token provided"));

        state = PluginState.INITIALIZED;
        logger.info("GitHub plugin initialized successfully");
    }

    @Override
    public void start() throws PluginException {
        logger.info("GitHub plugin Started successfully");
    }

    @Override
    public void stop() throws PluginException {
        logger.info("Stopping GitHub plugin");
        state = PluginState.STOPPED;
        logger.info("GitHub plugin stopped");
    }

    @Override
    public void destroy() {
        if (logger != null) {
            logger.info("Destroying GitHub plugin");
        }
        state = PluginState.UNLOADED;
    }

    @Override
    public PluginState getState() {
        return state;
    }

    public String getRepositoryInfo(String owner, String repo) {
        if (state != PluginState.STARTED) {
            throw new IllegalStateException("Plugin must be started to use GitHub operations");
        }
        logger.info("Fetching repository info for: " + owner + "/" + repo);
        return "Repository: " + owner + "/" + repo + " (Demo - GitHub API integration)";
    }

    public String createPullRequest(String owner, String repo, String title, String body, String head, String base) {
        if (state != PluginState.STARTED) {
            throw new IllegalStateException("Plugin must be started to use GitHub operations");
        }
        logger.info("Creating pull request: " + title + " in " + owner + "/" + repo);
        return "PR #123 created: " + title + " (Demo - GitHub API integration)";
    }

    public String listBranches(String owner, String repo) {
        if (state != PluginState.STARTED) {
            throw new IllegalStateException("Plugin must be started to use GitHub operations");
        }
        logger.info("Listing branches for: " + owner + "/" + repo);
        return "Branches: main, develop, feature/demo (Demo - GitHub API integration)";
    }
}

