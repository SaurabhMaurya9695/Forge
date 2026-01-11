package com.forge.shared.model.request;

import com.forge.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PluginInstallRequest {

    @NotBlank(message = ValidationConstants.VALIDATION_PLUGIN_NAME_REQUIRED)
    @Size(min = 1, max = 100, message = "Plugin name must be between 1 and 100 characters")
    private String pluginName;

    public PluginInstallRequest() {
    }

    public PluginInstallRequest(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }
}

