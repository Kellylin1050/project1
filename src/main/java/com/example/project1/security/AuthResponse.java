package com.example.project1.security;

import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthResponse {
    private String token;



    public AuthResponse(String token, String login_successful) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }
    public void setToken(){
        this.token = token;
    }
    public String getUsernameFromToken(String Token){
        return getToken();
    }


}
