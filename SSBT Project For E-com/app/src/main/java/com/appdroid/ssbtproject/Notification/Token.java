package com.appdroid.ssbtproject.Notification;

public class Token {

    String token,email;

    public Token(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public Token() {
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
}
