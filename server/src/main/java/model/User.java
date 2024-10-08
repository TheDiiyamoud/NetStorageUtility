package model;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
