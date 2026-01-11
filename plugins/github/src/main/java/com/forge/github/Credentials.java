package com.forge.github;

final class Credentials {

    private final Type type;
    private final String token;
    private final String username;
    private final String password;

    private Credentials(Type type, String token, String username, String password) {
        this.type = type;
        this.token = token;
        this.username = username;
        this.password = password;
    }

    static Credentials token(String token) {
        return new Credentials(Type.TOKEN, token, null, null);
    }

    static Credentials usernamePassword(String username, String password) {
        return new Credentials(Type.USERNAME_PASSWORD, null, username, password);
    }

    Type getType() {
        return type;
    }

    String getToken() {
        return token;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    enum Type {
        TOKEN, USERNAME_PASSWORD
    }
}

