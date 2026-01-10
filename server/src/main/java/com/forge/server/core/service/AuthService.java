package com.forge.server.core.service;

import com.forge.shared.model.response.LoginResponse;
import com.forge.shared.model.response.RegisterResponse;
import com.forge.server.core.service.authentication.AuthenticationService;
import com.forge.server.core.service.registration.UserRegistrationService;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRegistrationService registrationService;
    private final AuthenticationService authenticationService;

    /**
     * Constructor for AuthService
     *
     * @param registrationService   user registration service
     * @param authenticationService authentication service
     */
    public AuthService(UserRegistrationService registrationService, AuthenticationService authenticationService) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
    }

    /**
     * Registers a new user
     * Delegates to UserRegistrationService
     *
     * @param username username
     * @param email    email address
     * @param password plain text password
     * @return RegisterResponse containing user information
     */
    public RegisterResponse register(String username, String email, String password) {
        return registrationService.register(username, email, password);
    }

    /**
     * Authenticates user and generates JWT tokens
     * Delegates to AuthenticationService
     *
     * @param email    user email
     * @param password plain text password
     * @return LoginResponse containing user info and tokens
     */
    public LoginResponse login(String email, String password) {
        return authenticationService.authenticate(email, password);
    }
}
