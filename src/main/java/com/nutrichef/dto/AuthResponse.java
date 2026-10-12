package com.nutrichef.dto;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private UserProfileDto user;

    public AuthResponse() {}

    public AuthResponse(String token, UserProfileDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public UserProfileDto getUser() { return user; }
    public void setUser(UserProfileDto user) { this.user = user; }
}
