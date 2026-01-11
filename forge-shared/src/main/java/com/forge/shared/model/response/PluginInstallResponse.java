package com.forge.shared.model.response;

import java.time.LocalDateTime;

/**
 * Plugin Installation Response DTO
 * <p>
 * Data Transfer Object for plugin installation responses.
 * Contains plugin information and installation status.
 * <p>
 * This is a shared model that can be used across modules.
 *
 * @author Forge Team
 */
public class PluginInstallResponse {

    private String pluginName;
    private String version;
    private String state;
    private String message;
    private LocalDateTime installedAt;

    public PluginInstallResponse() {
        this.installedAt = LocalDateTime.now();
    }

    public PluginInstallResponse(String pluginName, String version, String state, String message) {
        this.pluginName = pluginName;
        this.version = version;
        this.state = state;
        this.message = message;
        this.installedAt = LocalDateTime.now();
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getInstalledAt() {
        return installedAt;
    }

    public void setInstalledAt(LocalDateTime installedAt) {
        this.installedAt = installedAt;
    }
}

