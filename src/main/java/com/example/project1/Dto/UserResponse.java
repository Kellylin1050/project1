package com.example.project1.Dto;

import java.util.Set;

public class UserResponse {
    private String name;
    private String email;
    private String username;
    private Set<String> roles;

    public UserResponse(String name, String email, String username, Set<String> roles) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
