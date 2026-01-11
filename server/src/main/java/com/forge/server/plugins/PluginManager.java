package com.forge.server.plugins;

import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.plugins.api.Plugin;
import com.forge.server.plugins.api.PluginContext;
import com.forge.server.plugins.api.PluginRegistry;
import com.forge.server.plugins.api.PluginState;
import com.forge.server.plugins.loader.PluginClassLoader;

import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Component
public class PluginManager implements PluginRegistry {

    private static final Logger logger = Logger.getLogger(PluginManager.class.getName());

    private static final String PLUGIN_SERVICE_FILE = "META-INF/services/com.forge.server.plugins.api.Plugin";
    private static final String DIST_LIB = "dist/lib";
    private static final String USER_DIR = "user.dir";
    private static final String JAR = ".jar";
    private static final String PLUGIN_JAR_FILE_NOT_FOUND = "Plugin JAR file not found: ";
    private static final String PLUGIN_CLASS_NOT_FOUND_IN_JAR = "Plugin class not found in JAR: ";
    private static final String CLASS_DOES_NOT_IMPLEMENT_PLUGIN_INTERFACE =
            "Class does not implement Plugin interface: ";
    private static final String PLUGIN_CLASS_NOT_FOUND = "Plugin class not found: ";
    private static final String PLUGIN_CLASS_MISSING_NO_ARGUMENT_CONSTRUCTOR =
            "Plugin class missing no-argument constructor: ";
    private static final String FAILED_TO_INSTANTIATE_PLUGIN = "Failed to instantiate plugin: ";
    private static final String ERROR_DURING_PLUGIN_INSTALLATION = "Unexpected error during plugin installation: ";

    // Thread-safe storage for plugins
    private final Map<String, PluginWrapper> plugins;

    // Thread-safe storage for ClassLoaders
    private final Map<String, PluginClassLoader> classLoaders;

    public PluginManager() {
        this.plugins = new ConcurrentHashMap<>();
        this.classLoaders = new ConcurrentHashMap<>();
    }

    @Override
    public Plugin getPlugin(String pluginName) {
        PluginWrapper wrapper = plugins.get(pluginName);
        return wrapper != null ? wrapper.getPlugin() : null;
    }

    public Plugin installPlugin(String pluginName) throws PluginException, IOException {
        long startTime = System.currentTimeMillis();
        Path jarFile = Paths.get(System.getProperty(USER_DIR), DIST_LIB, pluginName + JAR);

        if (!Files.exists(jarFile) || !Files.isRegularFile(jarFile)) {
            throw new PluginException(PLUGIN_JAR_FILE_NOT_FOUND + jarFile);
        }

        String className = discoverPluginClass(jarFile);
        if (className == null) {
            throw new PluginException(PLUGIN_CLASS_NOT_FOUND_IN_JAR + jarFile);
        }

        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
        PluginClassLoader classLoader = new PluginClassLoader(pluginName, jarFile, parentClassLoader);

        try {
            Class<?> pluginClass = classLoader.loadClass(className);
            if (!Plugin.class.isAssignableFrom(pluginClass)) {
                throw new PluginException(CLASS_DOES_NOT_IMPLEMENT_PLUGIN_INTERFACE + className);
            }

            @SuppressWarnings("unchecked") Class<? extends Plugin> pluginType = (Class<? extends Plugin>) pluginClass;
            Plugin plugin = pluginType.getDeclaredConstructor().newInstance();
            PluginContext context = new SimplePluginContext(pluginName);

            plugins.put(pluginName, new PluginWrapper(plugin, context));
            classLoaders.put(pluginName, classLoader);

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Plugin installed: " + pluginName + " v" + plugin.getVersion() + " in " + duration + "ms");
            return plugin;
        } catch (ClassNotFoundException e) {
            classLoader.close();
            throw new PluginException(PLUGIN_CLASS_NOT_FOUND + className, e);
        } catch (NoSuchMethodException e) {
            classLoader.close();
            throw new PluginException(PLUGIN_CLASS_MISSING_NO_ARGUMENT_CONSTRUCTOR + className, e);
        } catch (ReflectiveOperationException e) {
            classLoader.close();
            throw new PluginException(FAILED_TO_INSTANTIATE_PLUGIN + className, e);
        } catch (Exception e) {
            classLoader.close();
            if (e instanceof PluginException) {
                throw e;
            }
            throw new PluginException(ERROR_DURING_PLUGIN_INSTALLATION + e.getMessage(), e);
        }
    }

    private String discoverPluginClass(Path jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile.toFile())) {
            JarEntry serviceEntry = jar.getJarEntry(PLUGIN_SERVICE_FILE);
            if (serviceEntry != null) {
                try (InputStream is = jar.getInputStream(serviceEntry)) {
                    String className = new String(is.readAllBytes()).trim();
                    if (!className.isEmpty() && !className.startsWith("#")) {
                        return className.split("\n")[0].trim();
                    }
                }
            }

            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && !name.contains("$")) {
                    String className = name.replace("/", ".").replace(".class", "");
                    try {
                        ClassLoader tempLoader = new java.net.URLClassLoader(
                                new java.net.URL[] { jarFile.toUri().toURL() },
                                Thread.currentThread().getContextClassLoader());
                        Class<?> clazz = tempLoader.loadClass(className);
                        if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                            return className;
                        }
                    } catch (Exception ignored) {
                        // Ignore classes that cannot be loaded or don't implement Plugin
                    }
                }
            }
        }
        return null;
    }

    private static class SimplePluginContext implements PluginContext {

        private final String pluginName;
        private final Logger logger;

        public SimplePluginContext(String pluginName) {
            this.pluginName = pluginName;
            this.logger = Logger.getLogger("Plugin." + pluginName);
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
}
