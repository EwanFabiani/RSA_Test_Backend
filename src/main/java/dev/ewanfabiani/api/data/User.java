package dev.ewanfabiani.api.data;

public class User {

    private String username;
    private String modulus;
    private String exponent;

    public User(String username, String modulus, String exponent) {
        this.username = username;
        this.modulus = modulus;
        this.exponent = exponent;
    }

    public String getUsername() {
        return username;
    }

    public String getModulus() {
        return modulus;
    }

    public String getExponent() {
        return exponent;
    }

}
