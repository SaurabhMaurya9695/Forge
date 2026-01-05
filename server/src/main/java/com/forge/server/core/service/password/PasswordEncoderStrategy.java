package com.forge.server.core.service.password;

/**
 * Password Encoder Strategy Interface
 * <p>
 * Defines the contract for password encoding strategies.
 * Follows the Strategy pattern to allow different password encoding algorithms
 * to be used interchangeably at runtime.
 *
 * @author Forge Team
 */
public interface PasswordEncoderStrategy {

    /**
     * Encode a plain text password
     *
     * @param rawPassword the plain text password
     * @return the encoded password hash
     */
    String encode(String rawPassword);

    /**
     * Verify if a raw password matches the encoded password
     *
     * @param rawPassword     the plain text password
     * @param encodedPassword the encoded password hash
     * @return true if passwords match, false otherwise
     */
    boolean matches(String rawPassword, String encodedPassword);

    /**
     * Get the strategy name/identifier
     *
     * @return the name of this strategy (e.g., "BCRYPT", "ARGON2")
     */
    String getStrategyName();
}

