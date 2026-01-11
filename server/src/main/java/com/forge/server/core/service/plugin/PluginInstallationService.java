package com.forge.server.core.service.plugin;

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

    private static final String PLUGIN_INSTALLED_SUCCESSFULLY = "Plugin installed successfully";
    private static final String ERROR_PLUGIN_ALREADY_INSTALLED = "Plugin '%s' is already installed";
    private static final String ERROR_PLUGIN_INSTALLATION_FAILED = "Plugin installation failed: %s";
    private static final String ERROR_PLUGIN_NOT_FOUND_AFTER_INSTALLATION = "Plugin not found after installation";

    private static final Logger logger = LoggerFactory.getLogger(PluginInstallationService.class);
    private static final String ERROR_DURING_PLUGIN_INSTALLATION_FOR =
            "I/O error during plugin installation for '{}': {}";
    private static final String PLUGIN_INSTALLATION_FAILED_FOR = "Plugin installation failed for '{}': {}";
    private static final String I_O_ERROR = "I/O error: ";
    public static final String INSTALLING_PLUGIN_NAME_JAR_PATH_CLASS_NAME =
            "Installing plugin: name={}, jarPath={}, className={}";
    public static final String PLUGIN_IS_ALREADY_INSTALLED = "Plugin '{}' is already installed";

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
        logger.info(INSTALLING_PLUGIN_NAME_JAR_PATH_CLASS_NAME, pluginName, jarPath, className);

        try {
            // Check if plugin is already installed
            Plugin existingPlugin = pluginManager.getPlugin(pluginName);
            if (existingPlugin != null) {
                logger.warn(PLUGIN_IS_ALREADY_INSTALLED, pluginName);
                throw new PluginException(String.format(ERROR_PLUGIN_ALREADY_INSTALLED, pluginName));
            }

            // Delegate to PluginManager for installation
            Plugin installedPlugin;
            try {
                installedPlugin = pluginManager.installPlugin(pluginName, jarPath, className);
            } catch (java.io.IOException e) {
                logger.error(ERROR_DURING_PLUGIN_INSTALLATION_FOR, pluginName, e.getMessage(), e);
                throw new PluginException(String.format(ERROR_PLUGIN_INSTALLATION_FAILED, I_O_ERROR + e.getMessage()),
                        e);
            }

            // Build response
            PluginInstallResponse response = new PluginInstallResponse();
            response.setPluginName(installedPlugin.getName());
            response.setVersion(installedPlugin.getVersion());
            response.setState(
                    installedPlugin.getState() != null ? installedPlugin.getState().name() : PluginState.LOADED.name());
            response.setMessage(PLUGIN_INSTALLED_SUCCESSFULLY);

            logger.info("Plugin '{}' installed successfully", pluginName);
            return response;
        } catch (PluginException e) {
            logger.error(PLUGIN_INSTALLATION_FAILED_FOR, pluginName, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(PLUGIN_INSTALLATION_FAILED_FOR, pluginName, e.getMessage(), e);
            throw new PluginException(String.format(ERROR_PLUGIN_INSTALLATION_FAILED, e.getMessage()), e);
        }
    }
}

