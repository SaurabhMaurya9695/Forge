package com.forge.server.core.repository;

import com.forge.server.core.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * User Repository
 * <p>
 * Data access layer for User entities.
 * Provides CRUD operations and custom query methods.
 * Follows the Repository pattern for data access abstraction.
 *
 * @author Forge Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by username
     *
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by username
     *
     * @param username the username to check
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if user exists by email
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}

