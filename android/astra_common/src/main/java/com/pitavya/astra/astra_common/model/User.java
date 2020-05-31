package com.pitavya.astra.astra_common.model;

public class User {
    public String token, email, password, licenseKey, error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public User(String email, String password, String licenseKey) {
        this.email = email;
        this.password = password;
        this.licenseKey = licenseKey;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
