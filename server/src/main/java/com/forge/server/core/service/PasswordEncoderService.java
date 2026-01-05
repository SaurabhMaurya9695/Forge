package com.forge.server.core.service;

import com.forge.server.core.service.password.PasswordEncoderStrategy;
import com.forge.server.core.service.password.PasswordEncoderStrategyFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Password Encoder Service
 * <p>
 * Service responsible for encoding and verifying passwords.
 * Uses the Strategy pattern to support multiple password encoding algorithms.
 * The strategy can be configured at runtime via application properties.
 *
 * @author Forge Team
 */
@Service
public class PasswordEncoderService {

    private final PasswordEncoderStrategyFactory strategyFactory;
    private final PasswordEncoderStrategy strategy;

    /**
     * Constructor that initializes the password encoder service
     *
     * @param strategyFactory the factory to get password encoder strategies
     * @param strategyName    the name of the strategy to use (from configuration)
     */
    public PasswordEncoderService(PasswordEncoderStrategyFactory strategyFactory,
            @Value("${forge.security.password-encoder.strategy:BCRYPT}") String strategyName) {
        this.strategyFactory = strategyFactory;
        this.strategy = strategyFactory.getStrategy(strategyName);
    }

    /**
     * Encode a plain text password using the configured strategy
     *
     * @param rawPassword the plain text password
     * @return the encoded password hash
     */
    public String encode(String rawPassword) {
        return strategy.encode(rawPassword);
    }

    /**
     * Verify if a raw password matches the encoded password
     *
     * @param rawPassword     the plain text password
     * @param encodedPassword the encoded password hash
     * @return true if passwords match, false otherwise
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return strategy.matches(rawPassword, encodedPassword);
    }

    /**
     * Get the currently active strategy name
     *
     * @return the name of the current strategy
     */
    public String getActiveStrategyName() {
        return strategy.getStrategyName();
    }

    /**
     * Get all available strategies
     *
     * @return list of available strategy names
     */
    public java.util.List<String> getAvailableStrategies() {
        return strategyFactory.getAvailableStrategies();
    }
}
