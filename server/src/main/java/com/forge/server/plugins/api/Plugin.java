package com.forge.server.plugins.api;

import com.forge.server.common.plugin.exception.PluginException;

public interface Plugin {

    String getName();

    String getVersion();

    void init(PluginContext pluginContext) throws PluginException;

    void start() throws PluginException;

    void stop() throws PluginException;

    void destroy();

    PluginState getState();
}
