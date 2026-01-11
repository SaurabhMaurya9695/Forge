package com.forge.shared.model.request;

import com.forge.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Plugin Installation Request DTO
 * <p>
 * Data Transfer Object for plugin installation requests.
 * Contains validation annotations for input validation.
 * <p>
 * This is a shared model that can be used across modules.
 *
 * @author Forge Team
 */
public class PluginInstallRequest {

    @NotBlank(message = ValidationConstants.VALIDATION_PLUGIN_NAME_REQUIRED)
    @Size(min = 1, max = 100, message = "Plugin name must be between 1 and 100 characters")
    private String pluginName;

    @NotBlank(message = ValidationConstants.VALIDATION_JAR_PATH_REQUIRED)
    @Size(min = 1, max = 500, message = "JAR path must be between 1 and 500 characters")
    private String jarPath;

    @NotBlank(message = ValidationConstants.VALIDATION_CLASS_NAME_REQUIRED)
    @Size(min = 1, max = 500, message = "Class name must be between 1 and 500 characters")
    private String className;

    public PluginInstallRequest() {
    }

    public PluginInstallRequest(String pluginName, String jarPath, String className) {
        this.pluginName = pluginName;
        this.jarPath = jarPath;
        this.className = className;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

