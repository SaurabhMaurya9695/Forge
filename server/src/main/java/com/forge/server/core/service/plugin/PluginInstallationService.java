package com.forge.server.core.service.plugin;

import com.forge.common.constants.MessageConstants;
import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.plugins.PluginManager;
import com.forge.server.plugins.api.Plugin;
import com.forge.server.plugins.api.PluginState;
import com.forge.shared.model.response.PluginInstallResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Plugin Installation Service
 *
 * @author Forge Team
 */
@Service
public class PluginInstallationService {

    private static final Logger logger = LoggerFactory.getLogger(PluginInstallationService.class);

    private final PluginManager pluginManager;

    /**
     * Constructor for PluginInstallationService
     *
     * @param pluginManager plugin manager instance
     */
    public PluginInstallationService(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    /**
     * Installs a plugin
     * <p>
     * This method:
     * 1. Validates that plugin is not already installed
     * 2. Delegates to PluginManager for installation
     * 3. Transforms plugin information to response DTO
     * 4. Handles errors appropriately
     *
     * @param pluginName name of the plugin
     * @param jarPath    path to the plugin JAR file
     * @param className  fully qualified class name of the plugin implementation
     * @return PluginInstallResponse containing plugin information
     * @throws PluginException if installation fails
     */
    public PluginInstallResponse installPlugin(String pluginName, String jarPath, String className) {
        logger.info("Installing plugin: name={}, jarPath={}, className={}", pluginName, jarPath, className);

        try {
            // Check if plugin is already installed
            Plugin existingPlugin = pluginManager.getPlugin(pluginName);
            if (existingPlugin != null) {
                logger.warn("Plugin '{}' is already installed", pluginName);
                throw new PluginException(String.format(MessageConstants.ERROR_PLUGIN_ALREADY_INSTALLED, pluginName));
            }

            // Delegate to PluginManager for installation
            String result = pluginManager.installPlugin(pluginName, jarPath, className);

            // Get the installed plugin to retrieve metadata
            Plugin installedPlugin = pluginManager.getPlugin(pluginName);
            if (installedPlugin == null) {
                logger.error("Plugin '{}' installation returned success but plugin not found", pluginName);
                throw new PluginException(String.format(MessageConstants.ERROR_PLUGIN_INSTALLATION_FAILED,
                        "Plugin not found after installation"));
            }

            // Build response
            PluginInstallResponse response = new PluginInstallResponse();
            response.setPluginName(installedPlugin.getName());
            response.setVersion(installedPlugin.getVersion());
            response.setState(
                    installedPlugin.getState() != null ? installedPlugin.getState().name() : PluginState.LOADED.name());
            response.setMessage(MessageConstants.PLUGIN_INSTALLED_SUCCESSFULLY);

            logger.info("Plugin '{}' installed successfully", pluginName);
            return response;
        } catch (PluginException e) {
            logger.error("Plugin installation failed for '{}': {}", pluginName, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during plugin installation for '{}': {}", pluginName, e.getMessage(), e);
            throw new PluginException(String.format(MessageConstants.ERROR_PLUGIN_INSTALLATION_FAILED, e.getMessage()),
                    e);
        }
    }
}

