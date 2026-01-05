package com.forge.server.core.service;

import com.forge.server.core.entity.User;

public interface UserServiceInterface {

    User registerUser(String username, String email, String password);
}
