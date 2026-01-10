package com.forge.server.core.service;

import com.forge.server.core.entity.User;

import java.util.Optional;

public interface UserServiceInterface {

    /**
     * Register a new user
     *
     * @param username the username
     * @param email    the email address
     * @param password the plain text password
     * @return the created user entity
     */
    User registerUser(String username, String email, String password);

    /**
     * Find user by email
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
}
