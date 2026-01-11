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

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Plugin Manager
 * <p>
 * Manages plugin lifecycle and provides thread-safe plugin operations.
 * Registered as a Spring component for dependency injection.
 * Initializes lazily on Spring startup using @PostConstruct.
 *
 * @author Forge Team
 */
@Component
public class PluginManager implements PluginRegistry {

    private static final String PLUGIN_MANAGER_INITIALIZED = "Plugin Manager Initialized";

    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    public static final String PLUGIN_JAR_FILE_NOT_FOUND = "Plugin JAR file not found: ";
    public static final String JAR = ".jar";
    public static final String INVALID_FILE_TYPE_EXPECTED_JAR_FILE = "Invalid file type. Expected .jar file: ";
    public static final String PLUGIN_CLASS_NOT_FOUND = "Plugin class not found: ";
    public static final String PLUGIN_CLASS_DOES_NOT_HAVE_A_NO_ARGUMENT_CONSTRUCTOR =
            "Plugin class does not have a no-argument constructor: ";
    public static final String FAILED_TO_INSTANTIATE_PLUGIN = "Failed to instantiate plugin: ";
    public static final String UNEXPECTED_ERROR_DURING_PLUGIN_INSTALLATION =
            "Unexpected error during plugin installation: ";

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
    }

    @Override
    public Plugin getPlugin(String pluginName) {
        PluginWrapper wrapper = plugins.get(pluginName);
        return wrapper != null ? wrapper.getPlugin() : null;
    }

    /**
     * Installs a plugin from a JAR file.
     *
     * @param pluginName name of the plugin
     * @param jarPath    path to the plugin JAR file (can be relative or absolute)
     * @param className  fully qualified class name of the plugin implementation
     * @return the installed Plugin instance
     * @throws PluginException if installation fails
     * @throws IOException     if I/O error occurs while reading the JAR file
     */
    public Plugin installPlugin(String pluginName, String jarPath, String className)
            throws PluginException, IOException {

        long startTime = System.currentTimeMillis();

        // Resolve JAR file path
        Path jarFile = resolveJarPath(jarPath);

        // Validate JAR file exists
        if (!Files.exists(jarFile) || !Files.isRegularFile(jarFile)) {
            throw new PluginException(PLUGIN_JAR_FILE_NOT_FOUND + jarFile);
        }

        // Validate JAR file extension
        if (!jarFile.toString().endsWith(JAR)) {
            throw new PluginException(INVALID_FILE_TYPE_EXPECTED_JAR_FILE + jarFile);
        }

        // Get parent class loader
        ClassLoader parentClassLoader = getParentClassLoader();

        // Create plugin class loader
        PluginClassLoader classLoader = new PluginClassLoader(pluginName, jarFile, parentClassLoader);

        try {
            // Load and validate plugin class
            Class<?> pluginClass = classLoader.loadClass(className);
            if (!Plugin.class.isAssignableFrom(pluginClass)) {
                throw new PluginException("Class " + className + " does not implement Plugin interface");
            }

            // Instantiate plugin
            @SuppressWarnings("unchecked") Class<? extends Plugin> pluginType = (Class<? extends Plugin>) pluginClass;
            Plugin plugin = pluginType.getDeclaredConstructor().newInstance();

            // Create plugin context
            PluginContext context = createPluginContext(pluginName, plugin);

            // Register plugin
            plugins.put(pluginName, new PluginWrapper(plugin, context));
            classLoaders.put(pluginName, classLoader);

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Plugin '{}' v{} installed successfully {}ms", pluginName, plugin.getVersion(), duration);

            return plugin;
        } catch (ClassNotFoundException e) {
            classLoader.close();
            throw new PluginException(PLUGIN_CLASS_NOT_FOUND + className, e);
        } catch (NoSuchMethodException e) {
            classLoader.close();
            throw new PluginException(PLUGIN_CLASS_DOES_NOT_HAVE_A_NO_ARGUMENT_CONSTRUCTOR + className, e);
        } catch (ReflectiveOperationException e) {
            classLoader.close();
            throw new PluginException(FAILED_TO_INSTANTIATE_PLUGIN + className, e);
        } catch (Exception e) {
            classLoader.close();
            if (e instanceof PluginException) {
                throw e;
            }
            throw new PluginException(UNEXPECTED_ERROR_DURING_PLUGIN_INSTALLATION + e.getMessage(), e);
        }
    }

    /**
     * Resolves the JAR file path (handles both relative and absolute paths).
     *
     * @param jarPath the JAR path (can be relative to dist/lib or absolute)
     * @return resolved Path to the JAR file
     */
    private Path resolveJarPath(String jarPath) {
        Path path = Paths.get(jarPath);

        // If absolute path, return as-is
        if (path.isAbsolute()) {
            return path;
        }

        // If relative, resolve relative to dist/lib directory
        return Paths.get(System.getProperty("user.dir"), "dist", "lib", jarPath);
    }

    /**
     * Gets the parent class loader for plugin class loaders.
     *
     * @return parent class loader
     */
    private ClassLoader getParentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Creates a plugin context for the given plugin.
     *
     * @param pluginName name of the plugin
     * @param plugin     plugin instance
     * @return PluginContext instance
     */
    private PluginContext createPluginContext(String pluginName, Plugin plugin) {
        return new SimplePluginContext(pluginName);
    }

    /**
     * Simple implementation of PluginContext.
     */
    private static class SimplePluginContext implements PluginContext {

        private final String pluginName;
        private final Logger logger;

        public SimplePluginContext(String pluginName) {
            this.pluginName = pluginName;
            this.logger = LoggerFactory.getLogger("Plugin " + pluginName);
        }

        @Override
        public String getPluginName() {
            return pluginName;
        }

        @Override
        public Optional<String> getConfig(String key) {
            // TODO: Implement configuration loading from config files
            return Optional.empty();
        }

        @Override
        public Logger getLogger() {
            return logger;
        }
    }
}
