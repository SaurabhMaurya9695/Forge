package com.forge.server.core.service.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Password Encoder Strategy Factory
 * <p>
 * Factory class that manages password encoder strategies and provides
 * the appropriate strategy based on configuration.
 * Follows the Factory pattern to create strategy instances.
 *
 * @author Forge Team
 */
@Component
public class PasswordEncoderStrategyFactory {

    private final Map<String, PasswordEncoderStrategy> strategies;
    private final PasswordEncoderStrategy defaultStrategy;

    /**
     * Constructor that autowires all available strategies
     *
     * @param strategyList list of all PasswordEncoderStrategy implementations
     */
    @Autowired
    public PasswordEncoderStrategyFactory(List<PasswordEncoderStrategy> strategyList) {
        // Create a map of strategy name to strategy instance
        this.strategies = strategyList.stream().collect(
                Collectors.toMap(PasswordEncoderStrategy::getStrategyName, Function.identity(),
                        (existing, replacement) -> existing));
        this.defaultStrategy = strategies.getOrDefault("BCRYPT", strategyList.get(0));
    }

    /**
     * Get strategy by name
     *
     * @param strategyName the name of the strategy (e.g., "BCRYPT", "PBKDF2")
     * @return the strategy instance, or default if not found
     */
    public PasswordEncoderStrategy getStrategy(String strategyName) {
        if (strategyName == null || strategyName.isEmpty()) {
            return getDefaultStrategy();
        }
        return strategies.getOrDefault(strategyName.toUpperCase(), defaultStrategy);
    }

    /**
     * Get the default strategy
     *
     * @return the default password encoder strategy
     */
    public PasswordEncoderStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    /**
     * Get all available strategy names
     *
     * @return list of available strategy names
     */
    public List<String> getAvailableStrategies() {
        return strategies.keySet().stream().sorted().collect(Collectors.toList());
    }
}

