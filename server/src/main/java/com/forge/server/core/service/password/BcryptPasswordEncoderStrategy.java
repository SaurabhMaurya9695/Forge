package com.forge.server.core.service.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt Password Encoder Strategy
 * <p>
 * Implementation using BCrypt algorithm for password hashing.
 * BCrypt is a widely-used, secure password hashing algorithm.
 *
 * @author Forge Team
 */
@Component("bcryptPasswordEncoderStrategy")
public class BcryptPasswordEncoderStrategy implements PasswordEncoderStrategy {

    private static final String STRATEGY_NAME = "BCRYPT";
    private final PasswordEncoder passwordEncoder;

    public BcryptPasswordEncoderStrategy() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String getStrategyName() {
        return STRATEGY_NAME;
    }
}

