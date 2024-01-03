package dev.ewanfabiani.api.models;

import dev.ewanfabiani.api.data.User;

public class UserModel {

    private String username;
    private String modulus;
    private String exponent;

    public String getUsername() {
        return username;
    }

    public String getModulus() {
        return modulus;
    }

    public String getExponent() {
        return exponent;
    }

    public User toUser() {
        return new User(username, modulus, exponent);
    }

}
