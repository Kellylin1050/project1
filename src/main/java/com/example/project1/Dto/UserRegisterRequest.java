package com.example.project1.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;


@Schema(description = "使用者註冊要求")
public class UserRegisterRequest {
    @Schema(description = "姓名", example = "Sandra")
    @NotBlank
    private String name;
    @Schema(description = "信箱", example = "Sandra_1849@gmail.com")
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;
    @Schema(description = "密碼", example = "djfi937v7")
    @NotBlank
    private String password;
    @Schema(description = "使用者名稱", example = "Sandra_lin")
    private String username;

    private Set<String> roles;

    // Getters and Setters
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}


