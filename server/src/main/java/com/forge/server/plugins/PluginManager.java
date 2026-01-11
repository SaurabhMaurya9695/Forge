package com.forge.server.plugins;

import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.plugins.api.Plugin;
import com.forge.server.plugins.api.PluginContext;
import com.forge.server.plugins.api.PluginRegistry;
import com.forge.server.plugins.api.PluginState;
import com.forge.server.plugins.loader.PluginClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Plugin Manager
 * <p>
 * Manages plugin lifecycle and provides thread-safe plugin operations.
 * Registered as a Spring component for dependency injection.
 *
 * @author Forge Team
 */
@Component
public class PluginManager implements PluginRegistry {

    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    private static final String PLUGIN_MANAGER_INITIALIZED = "Plugin Manager Initialized";

    // Thread-safe storage for plugins
    private final Map<String, PluginWrapper> plugins;

    // Thread-safe storage for ClassLoaders
    private final Map<String, PluginClassLoader> classLoaders;

    private static class PluginWrapper {

        private final Plugin plugin;
        private PluginState state;
        private PluginContext context;

        public PluginWrapper(Plugin plugin, PluginContext context) {
            this.plugin = plugin;
            this.state = PluginState.LOADED;
            this.context = context;
        }

        public Plugin getPlugin() {
            return plugin;
        }

        public PluginState getState() {
            return state;
        }

        public PluginContext getContext() {
            return context;
        }
    }

    /**
     * Creates a new PluginManager.
     */
    public PluginManager() {
        this.plugins = new ConcurrentHashMap<>();
        this.classLoaders = new ConcurrentHashMap<>();
        logger.info(PLUGIN_MANAGER_INITIALIZED);
    }

    @Override
    public Plugin getPlugin(String pluginName) {
        PluginWrapper wrapper = plugins.get(pluginName);
        return wrapper != null ? wrapper.getPlugin() : null;
    }

    public String installPlugin(String pluginName, String jarPath, String className) throws PluginException {
        return pluginName;
    }
}
