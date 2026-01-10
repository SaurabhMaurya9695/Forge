package com.forge.server.core.service.registration;

import com.forge.common.constants.MessageConstants;
import com.forge.shared.model.response.RegisterResponse;
import com.forge.server.core.entity.User;
import com.forge.server.core.service.UserService;

import org.springframework.stereotype.Service;

/**
 *
 * @author Forge Team
 */
@Service
public class UserRegistrationService {

    private final UserService userService;

    public UserRegistrationService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user
     *
     * @param username username
     * @param email    email address
     * @param password plain text password
     * @return RegisterResponse containing user information
     */
    public RegisterResponse register(String username, String email, String password) {
        User user = userService.registerUser(username, email, password);
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().toString());
        response.setCreatedAt(user.getCreatedAt());
        response.setMessage(MessageConstants.USER_REGISTERED_SUCCESSFULLY);
        return response;
    }
}

