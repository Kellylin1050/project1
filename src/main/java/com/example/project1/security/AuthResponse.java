package com.example.project1.security;

import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String login_Successful;

    private String refreshToken;

    public AuthResponse(String token,String refreshToken,String login_Successful) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.login_Successful = login_Successful;
    }

    public String getToken(){
        return token;
    }
    public void setToken(){
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getUsernameFromToken(String Token){
        return getToken();
    }
    public String getLoginSuccessful() {
        return login_Successful;
    }

    public void setLoginSuccessful(String login_Successful) {
        this.login_Successful = login_Successful;
    }


}
